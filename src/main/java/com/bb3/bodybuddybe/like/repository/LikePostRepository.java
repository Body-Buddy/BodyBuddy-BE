package com.bb3.bodybuddybe.like.repository;

import com.bb3.bodybuddybe.like.entity.LikePost;
import com.bb3.bodybuddybe.post.entity.Post;
import com.bb3.bodybuddybe.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikePostRepository extends JpaRepository<LikePost, Long> {
    Optional<LikePost> findByUserAndPost(User user, Post post);
}
