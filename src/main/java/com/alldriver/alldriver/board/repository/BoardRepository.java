package com.alldriver.alldriver.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.alldriver.alldriver.board.domain.Board;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("SELECT b from board b left join fetch b.user u left join fetch b.boardImages i " +
            "left join fetch b.carBoards cb left join fetch cb.car c " +
            "left join fetch b.locationBoards lb left join fetch lb.subLocation l " +
            "left join fetch b.jobBoards jb left join fetch jb.job j " +
            "order by b.createdAt DESC")
    Page<Board> findAll(Pageable pageable);
    @Query("SELECT b from board b left join fetch b.user u left join fetch b.boardImages i " +
            "left join fetch b.carBoards cb left join fetch cb.car c " +
            "left join fetch b.locationBoards lb left join fetch lb.subLocation l " +
            "left join fetch b.jobBoards jb left join fetch jb.job j " +
            "where c.id in :carIds " +
            "order by b.createdAt DESC")
    Page<Board> findByCars(Pageable pageable, @Param("carIds") List<Long> carIds);
    @Query("SELECT b from board b left join fetch b.user u left join fetch b.boardImages i " +
            "left join fetch b.carBoards cb left join fetch cb.car c " +
            "left join fetch b.locationBoards lb left join fetch lb.subLocation l " +
            "left join fetch b.jobBoards jb left join fetch jb.job j " +
            "where j.id in :jobIds " +
            "order by b.createdAt DESC")
    Page<Board> findByJobs(Pageable pageable, @Param("jobIds") List<Long> jobIds);
    @Query("SELECT b from board b left join fetch b.user u left join fetch b.boardImages i " +
            "left join fetch b.carBoards cb left join fetch cb.car c " +
            "left join fetch b.locationBoards lb left join fetch lb.subLocation l " +
            "left join fetch b.jobBoards jb left join fetch jb.job j " +
            "where l.id in :locationIds " +
            "order by b.createdAt DESC")
    Page<Board> findBySubLocations(Pageable pageable, @Param("locationIds") List<Long> LocationIds);
    @Query("SELECT b from board b left join fetch b.user u left join fetch b.boardImages i " +
            "left join fetch b.carBoards cb left join fetch cb.car c " +
            "left join fetch b.locationBoards lb left join fetch lb.subLocation l " +
            "left join fetch b.jobBoards jb left join fetch jb.job j " +
            "where l.mainLocation.id = :mainLocationId " +
            "order by b.createdAt DESC")
    Page<Board> findByMainLocation(Pageable pageable, @Param("mainLocationId") Long mainLocationId);

}
