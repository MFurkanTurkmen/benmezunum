package com.mft.benmezunum.util;

import com.mft.benmezunum.entity.Role;
import com.mft.benmezunum.entity.enums.ERole;
import com.mft.benmezunum.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class RunAndInit {

    private final RoleRepository roleRepository;

    @PostConstruct
    public void run(){
        new Thread(() -> {
            saveRole();
        }).start();
    }

    public void saveRole(){
        if (roleRepository.findAll().isEmpty()){
            roleRepository.saveAll(
                    List.of(new Role(ERole.TEACHER)
                            ,new Role(ERole.STUDENT)
                            ,new Role(ERole.ADMIN),
                            new Role(ERole.DEVELOPER),
                            new Role(ERole.PRINCIPAL),
                            new Role(ERole.VICE_PRINCIPAL)
                    )
            );

        }


    }
}
