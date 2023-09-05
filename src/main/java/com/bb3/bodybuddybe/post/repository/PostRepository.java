package com.bb3.bodybuddybe.post.repository;

import com.bb3.bodybuddybe.gym.entity.Gym;
import com.bb3.bodybuddybe.post.entity.Post;
import com.bb3.bodybuddybe.post.enums.CategoryEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository <Post, Long> {
    Page<Post> findAllByGym(Gym gym, Pageable pageable);

    Page<Post> findAllByCategory(CategoryEnum category, Pageable pageable);

    Page<Post> findByTitleContainingOrContentContaining(String keyword1, String keyword2, Pageable pageable);
}
