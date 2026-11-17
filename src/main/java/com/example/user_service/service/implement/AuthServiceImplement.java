package com.example.user_service.service.implement;

import com.example.user_service.common.BaseResponse;
import com.example.user_service.dto.request.*;
import com.example.user_service.entity.User;
import com.example.user_service.exception.BusinessException;
import com.example.user_service.client.ShopClient;
import com.example.user_service.client.dto.request.ShopCreateRequest;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.service.AuthService;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.data.redis.core.StringRedisTemplate;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.concurrent.TimeUnit;
import java.util.Date;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImplement implements AuthService {

    private final Keycloak keycloak ;

    @Value("${keycloak.realm}")
    private String realm ;

    @Value("${keycloak.auth-server-url}")
    private String serverUrl ;

    private final WebClient.Builder webClientBuilder;
    private final ShopClient shopClient ;

    @Value("${keycloak.resource}")
    private String clientId ;
    @Value("${keycloak.credentials.secret}")
    private String clientSecret ;

    private final UserRepository userRepository ;
    private final StringRedisTemplate redisTemplate;

    private static final String ROLE_CUSTOMER = "CUSTOMER";
    private static final String ROLE_SELLER = "SELLER";

    @Override
    @Transactional
    public void registerCustomer(CustomerRegisterRequest request) {

        KeycloakUserCreateRequest keycloakUserCreateRequest = new KeycloakUserCreateRequest (
                                                                    request.getUsername(),
                                                                    request.getEmail(),
                                                                    request.getPassword(),ROLE_CUSTOMER
                                                             ) ;

        String keycloakUserId = createKeycloakUser(keycloakUserCreateRequest) ;

        // 3. Lưu cái keycloakUserId này vào MySQL nội bộ của bạn
        User user = new User() ;

        user.setKeycloakUserId(keycloakUserId);
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(ROLE_CUSTOMER);
        user.setFullName(request.getFullName());
        user.setStatus("ACTIVE");

        userRepository.save(user) ;

    }

    /**
     * Khách hàng đăng ký trở thành người bán cần chờ quản trị viên xét duyệt hồ sơ (pending)
     * Trạng thái khi mới đăng kí là ACTIVE với role CUSTOMER
     * Khi quản trị viên duyệt hồ sơ thành công ,đổi vai trò thành SELLER và trạng thai gian hàng thành appended"
     * @param request
     */


    @Override
    @Transactional
    public void registerSeller(SellerRegisterRequest request) {
        String keycloakUserId = null ;
        try {
            KeycloakUserCreateRequest keycloakUserCreateRequest = new KeycloakUserCreateRequest(
                    request.getUsername(),
                    request.getEmail(),
                    request.getPassword(), ROLE_CUSTOMER
            );

            keycloakUserId = createKeycloakUser(keycloakUserCreateRequest);

            User user = new User();

            // Lưu thông tin tạm thời người dùng đăng kí trở thành người bán
            user.setKeycloakUserId(keycloakUserId);
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPhoneNumber(request.getPhoneNumber());
            user.setRole(ROLE_CUSTOMER);
            user.setFullName(request.getFullName());
            user.setStatus("ACTIVE");

            // Gửi thông tin tạo gian hàng đến shop-service
            ShopCreateRequest shopCreateRequest = new ShopCreateRequest(
                    keycloakUserId,
                    request.getShopName(),
                    request.getDescriptionShop(),
                    request.getFullName(),
                    request.getPhoneNumber(),
                    request.getProvinceCode(),
                    request.getProvinceName(),
                    request.getWardCode(),
                    request.getWardName(),
                    request.getDetailAddress()
            );

            BaseResponse<String> response = shopClient.createShop(shopCreateRequest);
            if (response.getMeta().getCode() != 200) {
                throw new BusinessException("Lỗi khi tao cửa hàng");
            }

            userRepository.save(user);
        } catch (Exception e) {
            if (keycloakUserId != null) {
                try {
                    keycloak.realm(realm).users().get(keycloakUserId).remove();
                } catch (Exception keycloakException) {

                }
            }

            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new RuntimeException(e);
        }

    }


    private String createKeycloakUser(KeycloakUserCreateRequest request) {

        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(request.getPassword());
        user.setCredentials(Collections.singletonList(credential));

        // Gọi API sang Keycloak
        Response response = keycloak.realm(realm).users().create(user);

        // Kiểm tra kết quả
        if (response.getStatus() != 201) {
            if (response.getStatus() == 409) {
                // Đóng response TRƯỚC khi throw
                response.close();
                throw new BusinessException(409,"Tên đăng nhập đã tồn tại trên hệ thống!");

            } else {
                // Đọc thông báo lỗi trước
                String errorMessage = response.readEntity(String.class);
                log.error("Lỗi tạo user trên Keycloak. Status: {}, Error: {}", response.getStatus(), errorMessage);

                // Đóng response TRƯỚC khi throw
                response.close();
                throw new BusinessException("Hệ thống xác thực đang gặp sự cố, vui lòng thử lại sau!");
            }
        }

        String path = response.getLocation().getPath();
        String keycloakUserId = path.substring(path.lastIndexOf('/') + 1);
        response.close();

        try {
            // 3.1. Tìm UUID nội bộ của Client thông qua TÊN CLIENT (Client ID)
            List<ClientRepresentation> clients = keycloak.realm(realm).clients().findByClientId(clientId);

            if (clients.isEmpty()) {
                throw new BusinessException(500, "Cấu hình hệ thống lỗi: Không tìm thấy client " + clientId);
            }

            // Rút trích UUID nội bộ
            String clientUuid = clients.get(0).getId();

            // 3.2. Lấy role từ request
            String targetRoleName = request.getRole() != null ? request.getRole() : "CUSTOMER";
            if (!List.of("ADMIN", "CUSTOMER", "SELLER").contains(targetRoleName)) {
                throw new BusinessException(400, "Role không hợp lệ trên hệ thống!");
            }

            // 3.3. Lấy thông tin Role và gán cho User
            RoleRepresentation clientRole = keycloak.realm(realm)
                    .clients().get(clientUuid)
                    .roles().get(targetRoleName)
                    .toRepresentation();

            keycloak.realm(realm).users().get(keycloakUserId)
                    .roles().clientLevel(clientUuid).add(Collections.singletonList(clientRole));

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Không thể gán role {} cho user {}: {}", request.getRole(), request.getUsername(), e.getMessage());
            throw new BusinessException(500, "Lỗi từ Keycloak: " + e.getMessage());
        }
        return keycloakUserId ;

    }
    @Override
    public Map<String,Object> login(UserLoginRequest request) {
        String username = request.getUsername() ;
        String password = request.getPassword();

        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new BusinessException("Tài khoản có tên đăng nhập " + username +" không tồn tại !"));
        if ("LOCKED".equals(user.getStatus())) {
            throw new BusinessException("Tài khoản người dùng có tên đăng nhập " + username + " hiện đang bị khóa");
        }

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("grant_type", "password");
        formData.add("username", username);
        formData.add("password", password);

        // 2. Gọi API chuẩn với Content-Type phù hợp
        Map<String, Object> response = webClientBuilder.build()
                .post()
                .uri(serverUrl + "/realms/" + realm + "/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();



        return response;
    }

    @Override
    public Map<String, Object> refreshToken(TokenRefreshRequest refreshToken) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret); // Bắt buộc truyền client secret
        formData.add("grant_type", "refresh_token");
        formData.add("refresh_token", refreshToken.getRefreshToken());

        // Gọi API Keycloak
        Map<String, Object> response = webClientBuilder.build()
                .post()
                .uri(serverUrl + "/realms/" + realm + "/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        return response;
    }

    @Override
    public void logout(String accessToken, String refreshToken) {
        try {
            // 1. Phân tích Access Token để lấy thời gian hết hạn (sử dụng thư viện auth0)
            DecodedJWT jwt = JWT.decode(accessToken);
            Date expiresAt = jwt.getExpiresAt();
            long ttl = expiresAt.getTime() - System.currentTimeMillis();

            // 2. Nếu token vẫn còn hạn, đưa vào danh sách đen trên Redis
            if (ttl > 0) {
                redisTemplate.opsForValue().set(
                        "BLACKLIST:" + accessToken,
                        "LOGOUT",
                        ttl,
                        TimeUnit.MILLISECONDS
                );
            }

            // 3. Vô hiệu hóa Refresh Token và Session trên Keycloak
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("client_id", clientId);
            formData.add("client_secret", clientSecret);
            formData.add("refresh_token", refreshToken);

            webClientBuilder.build()
                    .post()
                    .uri(serverUrl + "/realms/" + realm + "/protocol/openid-connect/logout")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .bodyValue(formData)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();

        } catch (Exception e) {
            log.error("Lỗi trong quá trình đăng xuất: {}", e.getMessage());
            throw new BusinessException(500, "Không thể xử lý yêu cầu đăng xuất");
        }
    }

    @Override
    public void changePassword(String username,String keycloakUserId ,ChangePasswordRequest request) {
        try {
            UserLoginRequest userLogin = new UserLoginRequest(username, request.getOldPassword());
            this.login(userLogin) ;
        } catch (WebClientResponseException.Unauthorized | WebClientResponseException.BadRequest e) {
            throw new BusinessException("Mật khẩu cũ không chính xác!");
        } catch (Exception e) {
            throw new BusinessException("Lỗi hệ thống khi xác thực mật khẩu cũ.");
        }

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(request.getNewPassword());
        credential.setTemporary(false);

        keycloak.realm(realm)
                .users()
                .get(keycloakUserId)
                .resetPassword(credential);
    }

}
