package com.bb3.bodybuddybe.image.repository;

import com.bb3.bodybuddybe.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}