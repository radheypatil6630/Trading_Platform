package com.stock.treading.modal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stock.treading.domain.USER_ROLE;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Indexed;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fullName;

    @Column(unique = true)
    public String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String mobileno;

    @Embedded
    private TwoFactorAuth twoFactorAuth = new TwoFactorAuth();

    private USER_ROLE role= USER_ROLE.ROLE_CUSTOMER;
}
