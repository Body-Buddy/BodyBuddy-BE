package com.bb3.bodybuddybe.comment.repository;

import com.bb3.bodybuddybe.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
