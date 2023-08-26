package com.bb3.bodybuddybe.post.repository;

import com.bb3.bodybuddybe.post.entity.Post;
import com.bb3.bodybuddybe.post.entity.PostCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PostRepository extends JpaRepository <Post, Long> {
    List<Post> findAllByGymId(Long id);

    List<Post> findByPostCategory(PostCategory postCategory);

    List<Post> findByTitle(String postTitle);
}
