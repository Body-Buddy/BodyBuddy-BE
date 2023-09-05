package com.bb3.bodybuddybe.common.oauth2.repository;

import com.bb3.bodybuddybe.common.oauth2.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByMemberId(Long id);

    void deleteByMemberId(Long id);

}