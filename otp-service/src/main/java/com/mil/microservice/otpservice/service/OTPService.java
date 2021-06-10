package com.mil.microservice.otpservice.service;

import com.mil.microservice.otpservice.db.entity.TempOTP;
import com.mil.microservice.otpservice.db.repository.TempOTPRepository;
import com.mil.microservice.otpservice.dto.EmailDto;
import com.mil.microservice.otpservice.dto.RegisterCheckDto;
import com.mil.microservice.otpservice.dto.RegisterVerificationDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Random;

@Log4j2
@Service
public class OTPService {
    private final TempOTPRepository tempOTPRepository;
    private final RedisTemplate redisTemplate;
    private final ChannelTopic channelTopic;

    @Autowired
    public OTPService(TempOTPRepository tempOTPRepository, RedisTemplate redisTemplate, ChannelTopic channelTopic) {
        this.tempOTPRepository = tempOTPRepository;
        this.redisTemplate = redisTemplate;
        this.channelTopic = channelTopic;
    }

    public void requestOTP(RegisterCheckDto registerCheckDto) {
        String email = registerCheckDto.getEmail();
        // CHECK OTP redis
        TempOTP tempOTPByEmail = tempOTPRepository.getFirstByEmail(email);
        if (tempOTPByEmail!=null) {
            tempOTPRepository.delete(tempOTPByEmail);
        }

        // generate random number / otp
        String randomOTP = generateOTP();
        log.debug("random OTP: {}", randomOTP);

        // save to redis
        TempOTP tempOTP = new TempOTP();
        tempOTP.setEmail(email);
        tempOTP.setOtp(randomOTP);
        tempOTPRepository.save(tempOTP);

        // send email to broker
        sendEmail(email, "Kode Verifikasi Anda " + randomOTP);
    }

    private void sendEmail(String to, String body) {
        log.debug("to: {}, body: {}", to, body);
        EmailDto emailDto = new EmailDto();
        emailDto.setTo(to);
        emailDto.setSubject("Kode Verifikasi");
        emailDto.setBody(body);
        redisTemplate.convertAndSend(channelTopic.getTopic(), emailDto);
    }

    private String generateOTP() {
        return new DecimalFormat("0000").format(new Random().nextInt(9999));
    }

    public ResponseEntity<?> verificationOTP(RegisterVerificationDto registerVerificationDto) {
        // Check by email
        TempOTP tempOTPByEmail = tempOTPRepository.getFirstByEmail(registerVerificationDto.getEmail());
        if(tempOTPByEmail==null) return ResponseEntity.notFound().build();

        // Validasi OTP
        if(!tempOTPByEmail.getOtp().equals(registerVerificationDto.getOtp())) return ResponseEntity.unprocessableEntity().build();

        return ResponseEntity.ok().build();
    }
}
