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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.web.board.Board;
import com.web.service.BoardService;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    @Autowired
    private BoardService boardService;

    // 업로드된 파일을 저장할 폴더 경로
    private static final String UPLOADED_FOLDER = "uploads/";

    @GetMapping("/all")
    public List<Board> getAllBoards() {
        return boardService.getAllBoards();
    }

    @GetMapping
    public Page<Board> getAllBoards(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return boardService.getAllBoards(page, size);
    }

    @GetMapping("/{id}")
    public Board getBoardById(@PathVariable Long id) {
        return boardService.getBoardById(id);
    }

    // 파일 업로드를 처리하는 엔드포인트
    @PostMapping
    public ResponseEntity<Board> createBoard(@RequestPart("board") Board board, @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            if (image != null && !image.isEmpty()) {
                byte[] bytes = image.getBytes();
                Path path = Paths.get(UPLOADED_FOLDER + image.getOriginalFilename());

                // 디렉토리가 존재하지 않으면 생성
                if (Files.notExists(path.getParent())) {
                    Files.createDirectories(path.getParent());
                }

                Files.write(path, bytes);

                // 이미지 URL을 설정
                board.setBoardImage("/" + UPLOADED_FOLDER + image.getOriginalFilename());
            } else {
                board.setBoardImage(null); // 이미지가 없을 경우 null로 설정
            }
            board.setBoardDate(new Date()); // 현재 날짜로 설정

            // `boardAuthor` 필드가 null인지 확인하고 기본값 설정
            if (board.getBoardAuthor() == null || board.getBoardAuthor().isEmpty()) {
                board.setBoardAuthor("Unknown");
            }

            Board savedBoard = boardService.saveBoard(board);
            return ResponseEntity.ok(savedBoard);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    // 게시글 삭제 엔드포인트 추가
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        boardService.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }

    // 제목으로 검색하는 엔드포인트 추가
    @GetMapping("/search")
    public Page<Board> searchBoardsByTitle(@RequestParam("keyword") String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return boardService.searchBoardsByTitle(keyword, page, size);
    }
    
    // 조회수 증가 엔드포인트 추가
    @PostMapping("/increment-views/{id}")
    public ResponseEntity<Void> incrementViews(@PathVariable Long id) {
        boardService.incrementViews(id);
        return ResponseEntity.ok().build();
    }
    
    // 카테고리별 게시글을 가져오는 엔드포인트 추가
    @GetMapping("/category")
    public Page<Board> getBoardsByCategory(@RequestParam("category") String category, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return boardService.getBoardsByCategory(category, page, size);
    }
    
    // 좋아요 증가 엔드포인트 추가
    @PostMapping("/like/{id}")
    public ResponseEntity<Void> likeBoard(@PathVariable Long id) {
        boardService.likeBoard(id);
        return ResponseEntity.ok().build();
    }
    
    // 좋아요 취소 엔드포인트 추가
    @PostMapping("/unlike/{id}")
    public ResponseEntity<Void> unlikeBoard(@PathVariable Long id) {
        boardService.unlikeBoard(id);
        return ResponseEntity.ok().build();
    }
}
