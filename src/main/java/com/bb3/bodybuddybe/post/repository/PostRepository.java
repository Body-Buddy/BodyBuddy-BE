package com.bb3.bodybuddybe.post.repository;

import com.bb3.bodybuddybe.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository <Post, Long> {
//    List<Post> findAllByGymId(Long id);
}
