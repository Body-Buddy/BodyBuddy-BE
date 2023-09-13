package com.bb3.bodybuddybe.common.oauth2.repository;

import com.bb3.bodybuddybe.common.oauth2.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

}