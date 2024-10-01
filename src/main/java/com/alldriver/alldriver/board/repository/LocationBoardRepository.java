package com.alldriver.alldriver.board.repository;

import com.alldriver.alldriver.board.domain.LocationBoard;
import com.alldriver.alldriver.board.dto.query.SubLocationQueryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationBoardRepository extends JpaRepository<LocationBoard, Long> {
    void deleteById(@Param("id") Long id);

    Optional<LocationBoard> findByBoardIdAndSubLocationId(@Param("boardId") Long boardId, @Param("subLocationId") Long subLocationId);

    @Query("select new com.alldriver.alldriver.board.dto.query.SubLocationQueryDto(lb.board.id, s.id, s.category, ml.id, ml.category) from location_board lb  " +
            "left join lb.subLocation s left join s.mainLocation ml " +
            "where lb.board.id in :boardIds ")
    List<SubLocationQueryDto> findByBoardIds(@Param("boardIds") List<Long> boardIds);
}
