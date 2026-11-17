package com.example.user_service.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {
    private final MessageSource messageSource ;
    private String getMessage(String key) {
        return messageSource.getMessage(
                key,
                null,
                "System error, please try again later",
                LocaleContextHolder.getLocale()
        );
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String message = getMessage("MethodArgumentNotValidException.message") ;
        List<String> systemMessage = exception
                .getBindingResult()
                .getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList()) ;
        int code = 400 ;
        ErrorResponse errorResponse = new ErrorResponse(message,systemMessage,code) ;
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST) ;
    }

    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<Object> handleBusinessException(BusinessException exception) {
        String message = getMessage("BusinessException.message");
        List<String> systemMessage = exception.getMessage().lines().toList();

        // 1. Lấy mã code động từ exception (ví dụ: 409, 404)
        int code = exception.getErrorCode();

        ErrorResponse errorResponse = new ErrorResponse(message, systemMessage, code);

        // 2. Chuyển đổi số int sang HttpStatus (nếu truyền số tào lao thì mặc định về 400)
        HttpStatus httpStatus = HttpStatus.resolve(code);
        if (httpStatus == null) {
            httpStatus = HttpStatus.BAD_REQUEST;
        }

        // 3. Trả về response với status động
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleException(Exception exception) {
        String message = getMessage("Exception.message") ;
        List<String> systemMessage = exception.getMessage().lines().toList();
        int code = 500 ;
        ErrorResponse errorResponse = new ErrorResponse(message,systemMessage,code) ;
        return new ResponseEntity<>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR) ;
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException exception) {
        String message = "Đăng nhập thất bại";
        List<String> systemMessage = List.of("Mật khẩu hoặc tài khoản sai");
        int code = 401;

        ErrorResponse errorResponse = new ErrorResponse(message, systemMessage, code);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({WebClientResponseException.class})
    public ResponseEntity<Object> handleWebClientException(WebClientResponseException ex) {
        // Bắt lỗi 400 từ Keycloak (Refresh token hết hạn hoặc sai)
        if (ex.getStatusCode() == HttpStatus.BAD_REQUEST) {
            String message = "Phiên đăng nhập đã hết hạn, vui lòng đăng nhập lại.";
            List<String> systemMessage = List.of("Refresh token hết hạn hoặc không hợp lệ");
            int code = 401;

            ErrorResponse errorResponse = new ErrorResponse(message, systemMessage, code);
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }

        // Các lỗi khác từ Keycloak
        String message = "Lỗi xác thực từ server.";
        List<String> systemMessage = List.of(ex.getMessage());
        int code = ex.getStatusCode().value();

        ErrorResponse errorResponse = new ErrorResponse(message, systemMessage, code);
        return new ResponseEntity<>(errorResponse, ex.getStatusCode());
    }

}
