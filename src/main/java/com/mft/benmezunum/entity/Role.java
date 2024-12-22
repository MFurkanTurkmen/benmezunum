package com.mft.benmezunum.entity;

import com.mft.benmezunum.entity.enums.ERole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "roles")
@AllArgsConstructor
public class Role extends BaseEntity{
    @Enumerated(EnumType.STRING)
    private ERole name;
}
