package com.bb3.bodybuddybe.gym.repository;

import com.bb3.bodybuddybe.gym.entity.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GymRepository extends JpaRepository<Gym, Long> {
}
