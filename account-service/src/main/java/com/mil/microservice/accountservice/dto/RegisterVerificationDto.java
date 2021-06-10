package com.mil.microservice.accountservice.dto;

import lombok.Data;

@Data
public class RegisterVerificationDto {
    private String email;
    private String otp;
}
