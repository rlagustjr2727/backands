package com.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.comment.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	
    List<Comment> findByBoardBoardSeq(Long boardSeq);
    
    
    // 특정 사용자 Id 에 해당하는 댓글 목록을 조회할 수 있게 됩니다.
    List<Comment> findByUserId(Long userId);
}