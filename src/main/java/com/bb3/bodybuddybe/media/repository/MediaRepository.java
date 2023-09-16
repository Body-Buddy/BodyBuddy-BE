package com.bb3.bodybuddybe.media.repository;

import com.bb3.bodybuddybe.media.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
}