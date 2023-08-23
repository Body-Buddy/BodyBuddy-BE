package com.bb3.bodybuddybe.gym.repository;

import com.bb3.bodybuddybe.gym.entity.Gym;
import com.bb3.bodybuddybe.gym.entity.UserGym;
import com.bb3.bodybuddybe.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserGymRepository extends JpaRepository<UserGym, Long> {
    List<UserGym> findAllByUser(User user);

    Optional<UserGym> findByUserAndGym(User user, Gym gym);

    List<UserGym> findAllByGymId(Long gymId);

    boolean existsByUserAndGym(User user, Gym gym);

    boolean existsByUserAndGymId(User user, Long gymId);
}
