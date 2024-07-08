package com.web.board;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.web.comment.Comment;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "board")
@Getter
@Setter
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "board_seq")
    @SequenceGenerator(name = "board_seq", sequenceName = "board_seq", allocationSize = 1)
    private Long boardSeq;

    @Column(name = "board_title", nullable = false)
    private String boardTitle;

    @Column(name = "board_category", nullable = false)
    private String boardCategory;

    @Column(name = "board_views", nullable = false)
    private Integer boardViews;

    @Column(name = "board_author", nullable = false)
    private String boardAuthor;

    @Column(name = "board_content", nullable = false)
    private String boardContent;

    @Column(name = "board_image")
    private String boardImage;

    @Temporal(TemporalType.DATE)
    @Column(name = "board_date")
    private Date boardDate = new Date(); // 기본값을 현재 날짜로 설정

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Comment> comments;

    @Transient
    private int commentCount;
    
    @Column(name = "board_likes", nullable = false)
    private int boardLikes = 0;
    
    public int getCommentCount() {
        return comments != null ? comments.size() : 0;
    }
}
