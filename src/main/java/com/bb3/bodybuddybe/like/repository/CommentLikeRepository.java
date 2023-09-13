package com.bb3.bodybuddybe.like.repository;

import com.bb3.bodybuddybe.comment.entity.Comment;
import com.bb3.bodybuddybe.like.entity.CommentLike;
import com.bb3.bodybuddybe.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    boolean existsByUserAndComment(User user, Comment comment);

    Optional<CommentLike> findByUserAndComment(User user, Comment comment);
}
