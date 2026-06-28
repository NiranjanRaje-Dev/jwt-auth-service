package com.urbanest.auth.Payloads;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class SignUpRequest {
    private String username;
    private String email;
    private String mobileNumber;
    private String password;
}
