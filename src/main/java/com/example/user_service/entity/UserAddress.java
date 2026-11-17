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
@Table(name = "user_addresses")
public class UserAddress extends BaseEntity implements Serializable {
    @Id
    @UuidGenerator
    @Column(name = "id")
    private String id ;

    @Column(name = "user_id")
    private String userId ;

    @Column(name = "recipient_name")
    private String recipientName ;

    @Column(name = "phone_number")
    private String phoneNumber ;

    @Column(name = "province_code")
    private String provinceCode ;

    @Column(name = "province_name")
    private String provinceName ;

    @Column(name = "ward_code")
    private String wardCode ;

    @Column(name = "ward_name")
    private String wardName ;

    @Column(name = "detail_address")
    private String detailAddress ;

    @Column(name = "is_default")
    private Boolean isDefault ;

}
