package com.bb3.bodybuddybe.comment.controller;

import com.bb3.bodybuddybe.comment.dto.CommentRequestDto;
import com.bb3.bodybuddybe.comment.dto.CommentUpdateRequestDto;
import com.bb3.bodybuddybe.comment.service.CommentServiceImpl;
import com.bb3.bodybuddybe.common.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentServiceImpl commentService;

    @PostMapping("/comment")
    public ResponseEntity<?> save(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CommentRequestDto requestDto){
        return ResponseEntity.ok(commentService.save(userDetails, requestDto));

    }

    @PutMapping("/comment/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CommentUpdateRequestDto requestDto){
        return ResponseEntity.ok(commentService.update(id, userDetails, requestDto));
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.delete(id, userDetails);
        return ResponseEntity.status(HttpStatus.OK).body("댓글 삭제 성공");
    }
}