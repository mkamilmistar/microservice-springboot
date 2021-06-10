package com.mil.microservice.accountservice.db.entity;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;

@Data
@RedisHash(value = "account", timeToLive = 3600)
public class TempAccount {

    @Id
    private String id;
    @Indexed
    private String email;
    private Boolean valid = false;
}
