package com.alldriver.alldriver.board.repository;

import com.alldriver.alldriver.board.domain.JobBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JobBoardRepository extends JpaRepository<JobBoard, Long> {
    void deleteById(@Param("id") Long id);
}
