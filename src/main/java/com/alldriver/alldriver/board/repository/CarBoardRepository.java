package com.alldriver.alldriver.board.repository;

import com.alldriver.alldriver.board.domain.CarBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarBoardRepository extends JpaRepository<CarBoard, Long> {
    void deleteById(@Param("id") Long id);

    Optional<CarBoard> findByBoardIdAndCarId(@Param("boardId") Long boardId, @Param("carId") Long carId);
}
