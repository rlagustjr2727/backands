package com.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.web.board.Board;
import com.web.board.Post;
import com.web.exception.ResourceNotFoundException;
import com.web.repository.BoardRepository;
import com.web.repository.BoardRepositoryCustom;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    @Qualifier("boardRepositoryCustomImpl")
    private BoardRepositoryCustom boardRepositoryCustom;

    public List<Board> getAllBoards() {
        List<Board> boards = boardRepository.findAll();
        boards.forEach(board -> board.setCommentCount(board.getCommentCount()));
        return boards;
    }
    
    public Page<Board> getAllBoards(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Board> boards = boardRepositoryCustom.findAllBoards(pageable);
        boards.forEach(board -> board.setCommentCount(board.getCommentCount()));
        return boards;
    }

    public Board getBoardBySeq(Long seq) {
        Board board = boardRepository.findById(seq)
            .orElseThrow(() -> new ResourceNotFoundException("Board not found for this seq :: " + seq));
        board.setCommentCount(board.getCommentCount());
        return board;
    }

    public Board saveBoard(Board board) {
        return boardRepository.save(board);
    }

    public Board updateBoard(Board board) {
        return boardRepository.save(board);
    }

    public void deleteBoard(Long seq) {
        boardRepository.deleteById(seq);
    }
    
    // 페이징
    public Page<Board> searchBoardsByTitle(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Board> boards = boardRepositoryCustom.searchBoardsByTitle(title, pageable);
        boards.forEach(board -> board.setCommentCount(board.getCommentCount()));
        return boards;
    }
    
    // 조회수 증가
    public void incrementViews(Long seq) {
        Board board = boardRepository.findById(seq)
            .orElseThrow(() -> new ResourceNotFoundException("Board not found for this seq :: " + seq));
        board.setBoardViews(board.getBoardViews() + 1);
        boardRepository.save(board);
    }
    
    // 카테고리별 게시글을 가져오는 메서드
    public Page<Board> getBoardsByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Board> boards = boardRepositoryCustom.findBoardsByCategory(category, pageable);
        boards.forEach(board -> board.setCommentCount(board.getCommentCount()));
        return boards;
    }
    
    // 좋아요 수 증가 메서드 추가
    public void likeBoard(Long seq) {
        Board board = boardRepository.findById(seq)
            .orElseThrow(() -> new ResourceNotFoundException("Board not found for this seq :: " + seq));
        board.setBoardLikes(board.getBoardLikes() + 1);
        boardRepository.save(board);
    }
    
    // 좋아요 취소 메서드 추가
    public void unlikeBoard(Long seq) {
        Board board = boardRepository.findById(seq)
            .orElseThrow(() -> new ResourceNotFoundException("Board not found for this seq :: " + seq));
        board.setBoardLikes(board.getBoardLikes() - 1);
        boardRepository.save(board);
    }
}
