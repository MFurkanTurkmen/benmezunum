package com.mft.benmezunum.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RateLimit extends BaseEntity {

    @Column(name = "count", nullable = false)
    private Integer count;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "is_blocked", nullable = false)
    private boolean isBlocked;

    @PrePersist
    private void prePersist() {
        this.count = 0;
        this.isBlocked = false;
    }
}
