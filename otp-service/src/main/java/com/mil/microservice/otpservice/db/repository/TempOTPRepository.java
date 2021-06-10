package com.mil.microservice.otpservice.db.repository;

import com.mil.microservice.otpservice.db.entity.TempOTP;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TempOTPRepository extends CrudRepository<TempOTP, String> {
    TempOTP getFirstByEmail(String email);
}
