package com.example.user_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity implements Serializable {
    @Id
    @UuidGenerator
    @Column(name = "id")
    private String id ;

    @Column(name = "keycloak_user_id")
    private String keycloakUserId ;

    @Column(name = "username")
    private String username ;

    @Column(name = "email")
    private String email ;

    @Column(name = "phone_number")
    private String phoneNumber ;

    @Column(name = "role")
    private String role ;

    @Column(name = "full_name")
    private String fullName ;

    @Column(name = "avatar_url")
    private String avatarUrl ;

    @Column(name = "status")
    private String status ;

}
