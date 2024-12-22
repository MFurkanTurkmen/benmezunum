package com.mft.benmezunum.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Auth extends BaseEntity {

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name="email",unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role roles ;

    @Column(name = "ip_address")
    private String ipAddress;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rate_limit_id")
    private RateLimit rateLimit;

}