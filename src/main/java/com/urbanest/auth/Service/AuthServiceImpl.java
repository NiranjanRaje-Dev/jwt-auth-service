package com.urbanest.auth.Service;

import com.urbanest.auth.Dto.LoginResponseDto;
import com.urbanest.auth.Entities.User;
import com.urbanest.auth.Payloads.LoginRequest;
import com.urbanest.auth.Payloads.SignUpRequest;
import com.urbanest.auth.Repository.UserRepository;
import com.urbanest.auth.Utility.JwtUtill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService{

    private final AuthenticationManager authenticationManager;
    private final JwtUtill jwtUtill;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtUtill jwtUtill, UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtill = jwtUtill;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public LoginResponseDto login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
        User user = (User) authentication.getPrincipal();
        String token = jwtUtill.generateAccessToken(user);
        return new LoginResponseDto(token);
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
            userRepo.save(newUser);
            return "User Registered";
        }
        return "User Not Registered";
    }
}
