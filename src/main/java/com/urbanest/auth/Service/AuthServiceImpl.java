package com.urbanest.auth.Service;

import com.urbanest.auth.Dto.LoginResponseDto;
import com.urbanest.auth.Entities.AuthToken;
import com.urbanest.auth.Entities.User;
import com.urbanest.auth.Payloads.LoginRequest;
import com.urbanest.auth.Payloads.SignUpRequest;
import com.urbanest.auth.Repository.TokenRepository;
import com.urbanest.auth.Repository.UserRepository;
import com.urbanest.auth.Utility.JwtUtill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService{

    private final AuthenticationManager authenticationManager;
    private final JwtUtill jwtUtill;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepo;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtUtill jwtUtill, UserRepository userRepo, PasswordEncoder passwordEncoder, TokenRepository tokenRepo) {
        this.authenticationManager = authenticationManager;
        this.jwtUtill = jwtUtill;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepo = tokenRepo;
    }


    @Override
    public LoginResponseDto login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
        User user = (User) authentication.getPrincipal();
        String token = jwtUtill.generateAccessToken(user);
        AuthToken refreshToken = createRefreshToken(request.getUsername());
        return new LoginResponseDto(token,refreshToken.getRefreshToken());
    }

    @Override
    public String signUp(SignUpRequest request) {
        Optional<User> user = userRepo.findByUsername(request.getUsername());
        if(!user.isPresent()){
            User newUser = new User();
            newUser.setUsername(request.getUsername());
            newUser.setMobileNumber(request.getMobileNumber());
            newUser.setEmail(request.getEmail());
            newUser.setPassword(passwordEncoder.encode(request.getPassword()));
            newUser.setTokenVersion(1L);
            userRepo.save(newUser);
            return "User Registered";
        }
        return "User Not Registered";
    }

    @Override
    public LoginResponseDto refresh(LoginRequest request) {
        Optional<AuthToken> token = tokenRepo.findByRefreshToken(request.getRefreshToken());
        if(token.isPresent()){
            if(token.get().getExpiry().compareTo(Instant.now()) > 0){
                String newAccessToken = jwtUtill.generateAccessToken(token.get().getUser());
                return new LoginResponseDto(newAccessToken,request.getRefreshToken());
            }
            tokenRepo.delete(token.get());
        }
        return null;
    }


    public AuthToken createRefreshToken(String username){
        Optional<User> user = userRepo.findByUsername(username);
        if(user.isPresent()){
            AuthToken refreshToken = new AuthToken();
            refreshToken.setUser(user.get());
            refreshToken.setRefreshToken(UUID.randomUUID().toString());
            refreshToken.setExpiry(Instant.now().plusMillis(600000));
            return tokenRepo.save(refreshToken);
        }
        return null;
    }
}
