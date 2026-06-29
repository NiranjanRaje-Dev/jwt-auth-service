package com.urbanest.auth.Repository;

import com.urbanest.auth.Entities.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<AuthToken,Long> {
    Optional<AuthToken> findByRefreshToken(String refreshToken);
}
