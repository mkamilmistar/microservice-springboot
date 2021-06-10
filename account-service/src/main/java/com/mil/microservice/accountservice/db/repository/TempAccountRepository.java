package com.mil.microservice.accountservice.db.repository;

import com.mil.microservice.accountservice.db.entity.TempAccount;
import org.springframework.data.repository.CrudRepository;

public interface TempAccountRepository extends CrudRepository<TempAccount, String> {
    TempAccount getFirstByEmail(String email);
}

