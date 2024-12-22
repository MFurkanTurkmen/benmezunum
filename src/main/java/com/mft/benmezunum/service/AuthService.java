package com.mft.benmezunum.service;

import com.mft.benmezunum.dto.request.LoginRequest;
import com.mft.benmezunum.dto.request.SignupTeacherRQ;
import com.mft.benmezunum.dto.response.LoginResponse;
import com.mft.benmezunum.entity.Auth;
import com.mft.benmezunum.entity.RateLimit;
import com.mft.benmezunum.entity.Role;
import com.mft.benmezunum.entity.enums.ERole;
import com.mft.benmezunum.exception.AllExceptions;
import com.mft.benmezunum.exception.customException.AuthenticationException;
import com.mft.benmezunum.mapper.AuthMapper;
import com.mft.benmezunum.repository.AuthRepository;
import com.mft.benmezunum.repository.RateLimitRepository;
import com.mft.benmezunum.repository.RoleRepository;
import com.mft.benmezunum.util.JwtTokenManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenManager jwtTokenManager;
    private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final long LOCK_TIME_DURATION = 30; // minutes
    private final RateLimitRepository rateLimitRepository;


    public void register(SignupTeacherRQ dto, HttpServletRequest request) {
        RateLimit rateLimit=rateLimitControl(request);
        if (authRepository.findOptionalByUsername(dto.getUsername()).isPresent()) {
            throw new AuthenticationException(AllExceptions.UNKNOWN_ERROR,"Kullanici adi zaten kullaniliyor");
        }

        Auth auth= AuthMapper.INSTANCE.map(dto);

        auth.setPassword(passwordEncoder.encode(dto.getPassword()));


        auth.setRoles(Set.of(roleRepository.findOptionalByName(ERole.TEACHER)
                .orElseThrow(() -> new AuthenticationException(AllExceptions.UNKNOWN_ERROR,"Role bulunamadi"))));

        auth.setIpAddress(rateLimit.getIpAddress());
        rateLimit.setCount(0);
        auth.setRateLimit(rateLimit);
        rateLimitRepository.save(rateLimit);
        authRepository.save(auth);
    }

    public LoginResponse login(LoginRequest dto) {
        Auth auth = authRepository.findOptionalByUsername(dto.getUsername())
                .orElseThrow(() -> new AuthenticationException(AllExceptions.UNKNOWN_ERROR,"kullanici bulunamadi"));

        if (!passwordEncoder.matches(dto.getPassword(), auth.getPassword())) {
            throw new AuthenticationException(AllExceptions.UNKNOWN_ERROR,"Hatali Şifre");
        }

        String accessToken = jwtTokenManager.createToken(auth.getUsername())
                .orElseThrow(() -> new AuthenticationException(AllExceptions.UNKNOWN_ERROR));

        return LoginResponse.builder().bearer(accessToken).build();
    }

    private RateLimit rateLimitControl(HttpServletRequest request){
        Optional<RateLimit> rateLimit= rateLimitRepository.findOptionalByIpAddress(request.getRemoteAddr());

        if (rateLimit.isEmpty()){
            return createRateLimit(request);
        }

        if (rateLimit.get().getCount()>=MAX_FAILED_ATTEMPTS){
            rateLimit.get().setBlocked(true);

            if (rateLimit.get().getUpdatedAt().plusMinutes(LOCK_TIME_DURATION).isBefore(LocalDateTime.now())){
                rateLimit.get().setCount(0);
                rateLimit.get().setBlocked(false);
                return updateRateLimit(rateLimit.get());
            }else {
                throw new AuthenticationException(AllExceptions.UNKNOWN_ERROR,
                        "ip adresiniz bloke edildi. " +
                                "Lutfen " + getRemainingLockTime(rateLimit.get().getUpdatedAt()) + " sonra tekrar deneyiniz");


            }

        }
        else {
            return updateRateLimit(rateLimit.get());
        }
    }

    private RateLimit createRateLimit(HttpServletRequest request) {
        RateLimit rateLimit = new RateLimit();
        rateLimit.setIpAddress(request.getRemoteAddr());
        return rateLimitRepository.save(rateLimit);
    }

    private RateLimit updateRateLimit(RateLimit rateLimit){
        rateLimit.setCount(rateLimit.getCount()+1);
        rateLimit.setUpdatedAt(LocalDateTime.now());
        return rateLimitRepository.save(rateLimit);
    }

    private String getRemainingLockTime(LocalDateTime updatedAt) {
        LocalDateTime unlockTime = updatedAt.plusMinutes(LOCK_TIME_DURATION);
        Duration duration = Duration.between(LocalDateTime.now(), unlockTime);

        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.toSeconds() % 60;

        return String.format("%02d saat, %02d dakika, %02d saniye", hours, minutes, seconds);
    }

}