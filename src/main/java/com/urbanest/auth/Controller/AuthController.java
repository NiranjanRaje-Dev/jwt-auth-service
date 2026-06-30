package com.urbanest.auth.Controller;

import com.urbanest.auth.Dto.LoginResponseDto;
import com.urbanest.auth.Payloads.LoginRequest;
import com.urbanest.auth.Payloads.SignUpRequest;
import com.urbanest.auth.Service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequest request){
        return new ResponseEntity<>(authService.signUp(request),HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequest request){
        return new ResponseEntity<>(authService.login(request), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody LoginRequest request){
        return new ResponseEntity<>(authService.logout(request), HttpStatus.OK);
    }

    @PostMapping("/logout/all-devices")
    public ResponseEntity<String> logoutAllDevices(@RequestBody LoginRequest request){
        return new ResponseEntity<>(authService.logoutAllDevices(request), HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(@RequestBody LoginRequest request){
        return new ResponseEntity<>(authService.refresh(request), HttpStatus.OK);
    }


}
