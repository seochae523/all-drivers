package com.alldriver.alldriver.board.repository;

import com.alldriver.alldriver.board.domain.SubLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubLocationRepository extends JpaRepository<SubLocation, Long> {
    @Query("select s from sub_location s where s.id in :ids")
    List<SubLocation> findByIds(@Param("ids") List<Long> ids);

    @Query("select s from sub_location s where s.mainLocation.id = :mainLocationId")
    List<SubLocation> findByMainLocation(@Param("mainLocationId") Long mainLocationId);
}
