package com.alldriver.alldriver.board.repository;

import com.alldriver.alldriver.board.domain.BoardBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardBookmarkRepository extends JpaRepository<BoardBookmark, Long> {

    @Query("select b from board_bookmark b where b.board.id=:boardId and b.user.userId=:userId")
    Optional<BoardBookmark> findByBoardIdAndUserId(@Param("boardId") Long boardId, @Param("userId") String userId);
}
