package com.alldriver.alldriver.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.alldriver.alldriver.board.domain.BoardImage;

import java.util.List;

@Repository
public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {
    @Query("select i from board_image i where i.board.id=:boardId")
    List<BoardImage> findByBoardId(@Param("boardId") Long boardId);

}
