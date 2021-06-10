package com.mil.microservice.accountservice.db.repository;

import com.mil.microservice.accountservice.db.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account getFirstByEmail(String email);
}
