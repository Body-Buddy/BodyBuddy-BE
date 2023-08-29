package com.bb3.bodybuddybe.like.repository;

import com.bb3.bodybuddybe.comment.entity.Comment;
import com.bb3.bodybuddybe.like.entity.LikeComment;
import com.bb3.bodybuddybe.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeCommentRepository extends JpaRepository<LikeComment, Long> {
    Optional<LikeComment> findByUserAndComment(User user, Comment comment);
}
