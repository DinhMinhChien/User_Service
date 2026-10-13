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
@Table(name = "addresses")
public class Address extends BaseEntity implements Serializable {
    @Id
    @UuidGenerator
    @Column(name = "id")
    private String id ;

    @Column(name = "username")
    private String username ;

    @Column(name = "recipient_name")
    private String recipientName ;

    @Column(name = "recipient_phone")
    private String recipientPhone ;

    // xã / phường / đặc khu
    @Column(name = "commune")
    private String commune ;

    // tỉnh / thành phố
    @Column(name = "province")
    private String province ;

    @Column(name = "address_line")
    private String addressLine ;

    @Column(name = "is_default")
    private Boolean isDefault ;

}
