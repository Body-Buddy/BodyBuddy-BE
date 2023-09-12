package com.bb3.bodybuddybe.common.oauth2.repository;

import com.bb3.bodybuddybe.common.oauth2.entity.BlacklistedToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistedTokenRepository extends CrudRepository<BlacklistedToken, String> {
}
