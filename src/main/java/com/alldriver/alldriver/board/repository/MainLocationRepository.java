package com.alldriver.alldriver.board.repository;

import com.alldriver.alldriver.board.domain.MainLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MainLocationRepository extends JpaRepository<MainLocation, Long> {
    Optional<MainLocation> findByCategory(@Param("category") String mainLocation);
}
