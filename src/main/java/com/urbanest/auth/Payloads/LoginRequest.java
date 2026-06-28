package com.urbanest.auth.Payloads;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
