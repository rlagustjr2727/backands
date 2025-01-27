package com.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.comment.Comment;
import com.web.exception.ResourceNotFoundException;
import com.web.repository.CommentRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    // 게시글 번호(seq)로 댓글 가져오기
    public List<Comment> getCommentsByBoardSeq(Long boardSeq) {
        return commentRepository.findByBoardBoardSeq(boardSeq);
    }

    // 댓글 저장
    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    // 댓글 삭제
    public void deleteComment(Long commentSeq) {
        Comment comment = commentRepository.findById(commentSeq)
            .orElseThrow(() -> new ResourceNotFoundException("Comment not found for this id :: " + commentSeq));
        commentRepository.delete(comment);
    }

    // 댓글 업데이트
    public Comment updateComment(Long commentSeq, Comment updatedComment) {
        Comment comment = commentRepository.findById(commentSeq)
            .orElseThrow(() -> new ResourceNotFoundException("Comment not found for this id :: " + commentSeq));
        comment.setCommentContent(updatedComment.getCommentContent());
        return commentRepository.save(comment);
    }
    
    // 사용자 ID 로 댓글목록 가져오기
    // CommentRepository의 findByUserId 메서드를 호출하여 사용자 ID로 댓글 목록을 가져옵니다.
	public List<Comment> findCommentsByUserId(Long userId) {
		return commentRepository.findByUserId(userId);
	}
}
