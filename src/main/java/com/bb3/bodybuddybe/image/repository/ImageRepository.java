package com.bb3.bodybuddybe.image.repository;

import com.bb3.bodybuddybe.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Boolean existsByImageUrlAndId(String fileName, Long id);
}