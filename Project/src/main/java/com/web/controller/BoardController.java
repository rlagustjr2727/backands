package com.web.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.web.board.Board;
import com.web.security.UserPrincipal;
import com.web.service.BoardService;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    @Autowired
    private BoardService boardService;
    
    // 업로드된 파일을 저장할 폴더 경로
    private static final String UPLOADED_FOLDER = "uploads/";

    // 모든 게시글을 가져오는 엔드포인트
    @GetMapping("/all")
    public List<Board> getAllBoards() {
        return boardService.getAllBoards();
    }

    // 페이지네이션을 통해 모든 게시글을 가져오는 엔드포인트
    @GetMapping
    public Page<Board> getAllBoards(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return boardService.getAllBoards(page, size);
    }

    // ID를 통해 특정 게시글을 가져오는 엔드포인트
    @GetMapping("/{seq}")
    public Board getBoardBySeq(@PathVariable Long seq) {
        return boardService.getBoardBySeq(seq);
    }

    // 파일 업로드를 처리하는 엔드포인트
    @PostMapping
    public ResponseEntity<Board> createBoard(
            @RequestPart("board") Board board,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            // 이미지가 존재하면 파일로 저장하고 URL 설정
            if (image != null && !image.isEmpty()) {
                byte[] bytes = image.getBytes();
                Path path = Paths.get(UPLOADED_FOLDER + image.getOriginalFilename());

                if (Files.notExists(path.getParent())) {
                    Files.createDirectories(path.getParent());
                }

                Files.write(path, bytes);

                // 이미지 URL 설정
                board.setBoardImage(UPLOADED_FOLDER + image.getOriginalFilename());
            } else {
                board.setBoardImage(null); // 이미지가 없을 경우 null로 설정
            }
            board.setBoardDate(new Date()); // 현재 날짜로 설정

            // `boardAuthor`와 `boardAuthorImage` 필드를 로그인한 사용자의 닉네임과 프로필 이미지로 설정
            board.setBoardAuthor(userPrincipal.getNickname());
            board.setBoardProfileImage(userPrincipal.getUserProfileImage());

            Board savedBoard = boardService.saveBoard(board);
            return ResponseEntity.ok(savedBoard);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    // 게시글 수정 엔드포인트
    @PutMapping("/{seq}")
    public ResponseEntity<Board> updateBoard(
            @PathVariable Long seq,
            @RequestPart("board") Board board,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        try {
            // 기존 게시글 가져오기
            Board existingBoard = boardService.getBoardBySeq(seq);

            // 로그인한 사용자가 게시글 작성자가 아닌 경우
            if (!existingBoard.getBoardAuthor().equals(userPrincipal.getNickname())) {
                return ResponseEntity.status(403).build(); // Forbidden
            }

            // 이미지가 존재하면 파일로 저장하고 URL 설정
            if (image != null && !image.isEmpty()) {
                byte[] bytes = image.getBytes();
                Path path = Paths.get(UPLOADED_FOLDER + image.getOriginalFilename());

                if (Files.notExists(path.getParent())) {
                    Files.createDirectories(path.getParent());
                }

                Files.write(path, bytes);

                // 이미지 URL 설정
                board.setBoardImage(UPLOADED_FOLDER + image.getOriginalFilename());
            } else {
                board.setBoardImage(existingBoard.getBoardImage()); // 기존 이미지 유지
            }
            board.setBoardSeq(seq); // 시퀀스 설정
            board.setBoardDate(existingBoard.getBoardDate()); // 기존 날짜 유지
            board.setBoardAuthor(existingBoard.getBoardAuthor()); // 기존 작성자 유지
            board.setBoardProfileImage(existingBoard.getBoardProfileImage()); // 기존 프로필 이미지 유지

            Board updatedBoard = boardService.updateBoard(board);
            return ResponseEntity.ok(updatedBoard);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    // 게시글 삭제 엔드포인트
    @DeleteMapping("/{seq}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long seq, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Board board = boardService.getBoardBySeq(seq);

        // 로그인한 사용자가 게시글 작성자가 아닌 경우
        if (!board.getBoardAuthor().equals(userPrincipal.getNickname())) {
            return ResponseEntity.status(403).build(); // Forbidden
        }

        boardService.deleteBoard(seq);
        return ResponseEntity.noContent().build();
    }

    // 제목으로 검색하는 엔드포인트
    @GetMapping("/search")
    public Page<Board> searchBoardsByTitle(@RequestParam("keyword") String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return boardService.searchBoardsByTitle(keyword, page, size);
    }
    
    // 조회수 증가 엔드포인트
    @PostMapping("/increment-views/{seq}")
    public ResponseEntity<Void> incrementViews(@PathVariable Long seq) {
        boardService.incrementViews(seq);
        return ResponseEntity.ok().build();
    }
    
    // 카테고리별 게시글을 가져오는 엔드포인트
    @GetMapping("/category")
    public Page<Board> getBoardsByCategory(@RequestParam("category") String category, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return boardService.getBoardsByCategory(category, page, size);
    }
    
    // 좋아요 증가 엔드포인트
    @PostMapping("/like/{seq}")
    public ResponseEntity<Void> likeBoard(@PathVariable Long seq) {
        boardService.likeBoard(seq);
        return ResponseEntity.ok().build();
    }
    
    // 좋아요 취소 엔드포인트
    @PostMapping("/unlike/{seq}")
    public ResponseEntity<Void> unlikeBoard(@PathVariable Long seq) {
        boardService.unlikeBoard(seq);
        return ResponseEntity.ok().build();
    }
    
    // 사용자가 작성한 게시글 목록 가져오는 엔드포인트 추가
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Board>> getUserBoard(@PathVariable Long userId) {
    	List<Board> userBoard = boardService.findBoardByUserId(userId);
    	return ResponseEntity.ok(userBoard);
    }
    
    // 사용자가 좋아요 누른 게시글 목록 가져오는 엔드포인트 추가
    @GetMapping("/like/{userId}")
    public ResponseEntity<List<Board>> getUserLikedBoard(@PathVariable Long userId){
    	List<Board> likedBoard = boardService.findLikedBoardByUserId(userId);
    	return ResponseEntity.ok(likedBoard);
    }
    
   
}
