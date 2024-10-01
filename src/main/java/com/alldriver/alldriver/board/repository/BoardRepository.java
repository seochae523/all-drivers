package com.alldriver.alldriver.board.repository;

import com.alldriver.alldriver.board.dto.response.BoardFindResponseDto;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.alldriver.alldriver.board.domain.Board;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>, BoardQueryDslRepository {
    @Query("select new com.alldriver.alldriver.board.dto.response.BoardFindResponseDto(b.id, b.title, u.userId, b.createdAt,(case when bu.userId=:userId then 1 else 0 end))from board b " +
            "left join b.user u "+
            "left join b.boardBookmarks bm "+
            "left join bm.user bu "+
            "where b.deleted=false " +
            "order by (case when bu.userId=:userId then 1 else 0 end) desc, b.createdAt desc")
    Page<BoardFindResponseDto> findAll(@Param("userId") String userId, Pageable pageable);

    @Query("select new com.alldriver.alldriver.board.dto.response.BoardFindResponseDto(b.id, b.title, u.userId, b.createdAt,(case when bu.userId=:userId then 1 else 0 end))from board b " +
            "left join b.user u "+
            "left join b.boardBookmarks bm "+
            "left join bm.user bu "+
            "where b.deleted=false and bu.userId=:userId " +
            "order by (case when bu.userId=:userId then 1 else 0 end) desc, b.createdAt desc")
    Page<BoardFindResponseDto> findMyBookmarkedBoard(@Param("userId") String userId, Pageable pageable);

    @Query("select b from board b " +
            "left join fetch b.boardImages i " +
            "left join fetch b.carBoards cb " +
            "left join fetch cb.car c " +
            "left join fetch b.jobBoards jb " +
            "left join fetch jb.job j " +
            "where b.id=:id")
    Optional<Board> findDetailById(Long id);

    // User ID에 따른 검색
    @Query("select new com.alldriver.alldriver.board.dto.response.BoardFindResponseDto(b.id, b.title, u.userId, b.createdAt,(case when bu.userId=:userId then 1 else 0 end))from board b " +
            "left join b.user u "+
            "left join b.boardBookmarks bm "+
            "left join bm.user bu "+
            "where b.deleted=false and u.userId=:userId " +
            "order by (case when bu.userId=:userId then 1 else 0 end) desc, b.createdAt desc")
    Page<BoardFindResponseDto> findByUserId(@Param("userId") String userId, Pageable pageable);




}
