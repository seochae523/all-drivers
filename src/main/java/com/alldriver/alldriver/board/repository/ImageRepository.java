package com.alldriver.alldriver.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.alldriver.alldriver.board.domain.Image;
@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
