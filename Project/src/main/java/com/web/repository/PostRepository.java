package com.web.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.board.Post;

public interface PostRepository extends JpaRepository<Post, Long>{
	
	

}
