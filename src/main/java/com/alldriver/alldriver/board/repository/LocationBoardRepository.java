package com.alldriver.alldriver.board.repository;

import com.alldriver.alldriver.board.domain.LocationBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationBoardRepository extends JpaRepository<LocationBoard, Long> {
    void deleteById(@Param("id") Long id);

    Optional<LocationBoard> findByBoardIdAndSubLocationId(@Param("boardId") Long boardId, @Param("subLocationId") Long subLocationId);
}
