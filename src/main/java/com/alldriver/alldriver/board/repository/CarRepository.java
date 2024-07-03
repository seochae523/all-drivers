package com.alldriver.alldriver.board.repository;

import com.alldriver.alldriver.board.domain.Car;
import com.alldriver.alldriver.board.domain.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    @Query("select c from car c where c.id in :ids")
    List<Car> findByIds(@Param("ids") List<Long> ids);
}
