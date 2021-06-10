package com.mil.microservice.accountservice.service;

import com.mil.microservice.accountservice.controller.AccountController;
import com.mil.microservice.accountservice.db.entity.Account;
import com.mil.microservice.accountservice.db.entity.TempAccount;
import com.mil.microservice.accountservice.db.repository.AccountRepository;
import com.mil.microservice.accountservice.db.repository.TempAccountRepository;
import com.mil.microservice.accountservice.dto.RegisterCheckDto;
import com.mil.microservice.accountservice.dto.RegisterVerificationDto;
import com.mil.microservice.accountservice.feignclient.OTPClient;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final TempAccountRepository tempAccountRepository;
    private final OTPClient otpClient;

    @Autowired
    public AccountService(AccountRepository accountRepository, TempAccountRepository tempAccountRepository, OTPClient otpClient) {
        this.accountRepository = accountRepository;
        this.tempAccountRepository = tempAccountRepository;
        this.otpClient = otpClient;
    }

    public ResponseEntity<?> registerCheck(RegisterCheckDto registerCheckDto) {
        String email = registerCheckDto.getEmail();
        // check data di psql
        Account accountByEmail = accountRepository.getFirstByEmail(email);
        if (accountByEmail!=null) return ResponseEntity.status(HttpStatus.CONFLICT).build();

        // check data in redis
        TempAccount tempAccountByEmail = tempAccountRepository.getFirstByEmail(email);
        if (tempAccountByEmail!=null) return ResponseEntity.ok().build();

        // save temp to redis
        tempAccountByEmail = new TempAccount();
        tempAccountByEmail.setEmail(email);
        tempAccountByEmail.setValid(false);
        tempAccountRepository.save(tempAccountByEmail);

        // Request OTP
        try {
            otpClient.requestOTP(registerCheckDto);
        } catch (FeignException.FeignClientException ex) {
            return ResponseEntity.status(ex.status()).body(ex.contentUTF8());
        }

        return ResponseEntity.ok().build();
    }

    public String testLoadBalancer() {
        return otpClient.testLoadBalancer();
    }

    public ResponseEntity<?> verification(RegisterVerificationDto registerVerificationDto) {
        // Check data in redis
        TempAccount tempAccountByEmail = tempAccountRepository.getFirstByEmail(registerVerificationDto.getEmail());
        if(tempAccountByEmail==null) return ResponseEntity.notFound().build();

        // Validasi OTP
        try {
            otpClient.verificationOTP(registerVerificationDto);
        } catch (FeignException.FeignClientException ex) {
            ex.printStackTrace();
            return ResponseEntity.unprocessableEntity().build();
        }


        // update Verification
        tempAccountByEmail.setValid(true);
        tempAccountRepository.save(tempAccountByEmail);

        return ResponseEntity.ok().build();
    }
}
