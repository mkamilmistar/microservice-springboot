package com.mil.microservice.otpservice.dto;

import lombok.Data;

@Data
public class RegisterVerificationDto {
    private String email;
    private String otp;
}
