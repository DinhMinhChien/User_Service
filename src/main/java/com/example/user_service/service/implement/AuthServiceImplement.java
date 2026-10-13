package com.example.user_service.service.implement;

import com.example.user_service.dto.request.ChangePasswordRequest;
import com.example.user_service.dto.request.UserLoginRequest;
import com.example.user_service.dto.request.UserRegisterRequest;
import com.example.user_service.dto.request.UserSaveRequest;
import com.example.user_service.entity.Address;
import com.example.user_service.entity.User;
import com.example.user_service.exception.BusinessException;
import com.example.user_service.mapper.UserMapper;
import com.example.user_service.repository.AddressRepository;
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

    @Value("${keycloak.resource}")
    private String clientId ;
    @Value("${keycloak.credentials.secret}")
    private String clientSecret ;

    private final UserRepository userRepository ;
    private final AddressRepository addressRepository ;
    private final UserMapper userMapper ;


    @Override
    @Transactional
    public void register(UserRegisterRequest request) {

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
        String keycloakUuid = path.substring(path.lastIndexOf('/') + 1);
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
            String targetRoleName = request.getRole() != null ? request.getRole() : "USER";
            if (!List.of("ADMIN", "USER", "SHIPPER").contains(targetRoleName)) {
                throw new BusinessException(400, "Role không hợp lệ trên hệ thống!");
            }

            // 3.3. Lấy thông tin Role và gán cho User
            RoleRepresentation clientRole = keycloak.realm(realm)
                    .clients().get(clientUuid)
                    .roles().get(targetRoleName)
                    .toRepresentation();

            keycloak.realm(realm).users().get(keycloakUuid)
                    .roles().clientLevel(clientUuid).add(Collections.singletonList(clientRole));

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Không thể gán role {} cho user {}: {}", request.getRole(), request.getUsername(), e.getMessage());
            throw new BusinessException(500, "Lỗi từ Keycloak: " + e.getMessage());
        }

        // 3. Lưu cái keycloakUserId này vào MySQL nội bộ của bạn
        UserSaveRequest userSaveRequest = userMapper.toUserSave(request) ;
        saveUser(userSaveRequest) ;

         // 4. Lưu địa chỉ của người dùng
        Address address = new Address() ;
        address.setUsername(request.getUsername());
        address.setRecipientName(request.getFullName());
        address.setRecipientPhone(request.getPhone());
        address.setCommune(request.getCommune());
        address.setProvince(request.getProvince());
        address.setAddressLine(request.getAddressLine());
        address.setIsDefault(Boolean.TRUE);
        addressRepository.save(address) ;

    }
    public void saveUser(UserSaveRequest request) {
        User localUser = new User();
        localUser.setUsername(request.getUsername());
        localUser.setFullName(request.getFullName());
        localUser.setPhone(request.getPhone()) ;
        localUser.setEmail(request.getEmail());
        localUser.setRole(request.getRole());
        userRepository.save(localUser);
    }

    @Override
    public Map<String,Object> login(UserLoginRequest request) {

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret); // Bỏ dòng này nếu Client trong Keycloak là Public
        formData.add("grant_type", "password");
        formData.add("username", request.getUsername());
        formData.add("password", request.getPassword());

        // 2. Gọi API chuẩn với Content-Type phù hợp
        Map<String, Object> response = webClientBuilder.build()
                .post()
                .uri(serverUrl + "/realms/" + realm + "/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED) // Bắt buộc phải có
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block(); // .block() dùng được nếu đây là ứng dụng Spring MVC truyền thống (blocking)

        return response;
    }

    @Override
    public void delete(String keyCloakId) {
        Response response = keycloak.realm(realm).users().delete(keyCloakId);

        // Keycloak trả về mã 204 (No Content) nếu xóa thành công
        if (response.getStatus() != 204) {
            String errorMessage = response.readEntity(String.class);
            response.close();
            throw new RuntimeException("Cannot delete user in Keycloak. Status: "
                    + response.getStatus() + ", error: " + errorMessage);
        }

        response.close();
        System.out.println("========== TEST KEYCLOAK ==========");
        System.out.println("Đã xóa thành công user có ID: " + keyCloakId);
        System.out.println("===================================");
    }

    @Override
    public void changePassword(String username,String userUuid ,ChangePasswordRequest request) {
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
                .get(userUuid)
                .resetPassword(credential);
    }

}
