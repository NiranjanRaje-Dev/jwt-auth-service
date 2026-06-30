package com.urbanest.auth.Repository;

import com.urbanest.auth.Entities.AuthToken;
import com.urbanest.auth.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<AuthToken,Long> {
    Optional<AuthToken> findByRefreshToken(String refreshToken);

    void deleteAllByUser(User user);
}
