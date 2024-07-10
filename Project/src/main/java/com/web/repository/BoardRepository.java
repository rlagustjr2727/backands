package com.web.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.web.board.Board;
import com.web.board.Post;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {
	
	Page<Board> findAllByOrderByBoardDateDesc(Pageable pageable);
	
	List<Board> findByUserId(Long userId); // 사용자 ID 로 게시글 목록 조회
	
	// 사용자가 좋아요 누른 게시글을 조회
    @Query("SELECT p FROM Post p JOIN p.likes l WHERE l.user.id = :userId")
    List<Board> findLikedBoardByUserId(@Param("userId") Long userId);

	
	

}