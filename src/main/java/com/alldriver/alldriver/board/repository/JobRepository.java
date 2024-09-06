package com.alldriver.alldriver.board.repository;

import com.alldriver.alldriver.board.domain.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {


    @Query("select j from job j where j.id in :ids")
    List<Job> findByIds(@Param("ids") List<Long> ids);
}
