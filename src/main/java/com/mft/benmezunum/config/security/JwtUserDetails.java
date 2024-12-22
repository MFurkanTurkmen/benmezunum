package com.mft.benmezunum.config.security;

import com.mft.benmezunum.entity.Auth;
import com.mft.benmezunum.exception.AllExceptions;
import com.mft.benmezunum.exception.customException.AuthenticationException;
import com.mft.benmezunum.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JwtUserDetails implements UserDetailsService {

    @Autowired
    AuthRepository authRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Auth> auth = authRepository.findOptionalByUsername(username);
        if (auth.isEmpty())
            throw new AuthenticationException(AllExceptions.USER_NOT_FOUND, username+"Kullanıcı bulunamadı.");
        return buildUserDetails(auth.get());
    }

    private UserDetails buildUserDetails(Auth auth) {
        List<GrantedAuthority> authorities = auth.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return User.builder()
                .username(auth.getUsername())
                .password(auth.getPassword())
                .authorities(authorities)
                .accountLocked(false)
                .disabled(false)
                .credentialsExpired(false)
                .build();
    }



}