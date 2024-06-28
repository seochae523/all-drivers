package com.alldriver.alldriver.board.repository;

import com.alldriver.alldriver.board.domain.SubLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<SubLocation, Long> {
}
