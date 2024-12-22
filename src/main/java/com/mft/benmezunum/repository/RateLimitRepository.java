package com.mft.benmezunum.repository;

import com.mft.benmezunum.entity.RateLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RateLimitRepository extends JpaRepository<RateLimit, Long> {
  Optional<RateLimit> findOptionalByIpAddress(String remoteAddr);
}