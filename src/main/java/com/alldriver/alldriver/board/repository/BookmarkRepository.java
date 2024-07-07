package com.alldriver.alldriver.board.repository;

import com.alldriver.alldriver.board.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @Query("select b from bookmark b where b.board.id=:boardId and b.user.userId=:userId")
    Optional<Bookmark> findByBoardIdAndUserId(@Param("boardId") Long boardId, @Param("userId") String userId);
}
