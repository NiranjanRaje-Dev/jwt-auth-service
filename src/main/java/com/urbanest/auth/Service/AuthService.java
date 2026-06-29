package com.urbanest.auth.Service;

import com.urbanest.auth.Dto.LoginResponseDto;
import com.urbanest.auth.Payloads.LoginRequest;
import com.urbanest.auth.Payloads.SignUpRequest;

public interface AuthService{
    LoginResponseDto login(LoginRequest request);

    String signUp(SignUpRequest request);

    LoginResponseDto refresh(LoginRequest request);
}
