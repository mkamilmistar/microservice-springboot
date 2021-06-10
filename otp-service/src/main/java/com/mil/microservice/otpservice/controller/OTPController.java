package com.mil.microservice.otpservice.controller;

import com.mil.microservice.otpservice.dto.RegisterCheckDto;
import com.mil.microservice.otpservice.dto.RegisterVerificationDto;
import com.mil.microservice.otpservice.service.OTPService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class OTPController {
    private OTPService otpService;
    private Environment environment;

    @Autowired
    public OTPController(OTPService otpService, Environment environment) {
        this.otpService = otpService;
        this.environment = environment;
    }

    @PostMapping("/request")
    public ResponseEntity<?> requestOTP(@RequestBody RegisterCheckDto registerCheckDto) {
        log.debug("request OTP: {}", registerCheckDto);
        otpService.requestOTP(registerCheckDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/test-loadbalancer")
    public String testLoadBalancer() {
        String port = environment.getProperty("local.server.port");
        log.debug("port: {}", port);
        return "oke with port: " + port;
    }

    @PostMapping("/verification")
    public ResponseEntity<?> verificationOTP(@RequestBody RegisterVerificationDto registerVerificationDto) {
        return otpService.verificationOTP(registerVerificationDto);
    }
}
