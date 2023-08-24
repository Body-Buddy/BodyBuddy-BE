package com.bb3.bodybuddybe.matching.repository;

import com.bb3.bodybuddybe.matching.entity.MatchingCriteria;
import com.bb3.bodybuddybe.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchingCriteriaRepository extends JpaRepository<MatchingCriteria, Long> {
    Optional<MatchingCriteria> findByUser(User user);
}
