package com.mil.microservice.accountservice.feignclient;

import com.mil.microservice.accountservice.dto.RegisterCheckDto;
import com.mil.microservice.accountservice.dto.RegisterVerificationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "otp")
public interface OTPClient {

    @PostMapping("/request")
    public ResponseEntity<?> requestOTP(@RequestBody RegisterCheckDto registerCheckDto);

    @GetMapping("/test-loadbalancer")
    public String testLoadBalancer();

    @PostMapping("/verification")
    public ResponseEntity<?> verificationOTP(@RequestBody RegisterVerificationDto registerVerificationDto);
}
