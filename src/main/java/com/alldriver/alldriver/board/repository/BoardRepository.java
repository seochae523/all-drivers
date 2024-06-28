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
    List<Board> findTop10ByIdGreaterThan(@Param("id") Long id);

    @Query("SELECT b from board b left join fetch b.user u join fetch board_image i where b.id=:id")
    Optional<Board> findByIdWithUserAndImage(@Param("id") Long id);

    @Query("SELECT b from board b left join fetch b.user u left join fetch b.boardImages i " +
            "left join fetch b.carBoards cb left join fetch cb.car c " +
            "left join fetch b.placeBoards pb left join fetch pb.place p " +
            "left join fetch b.jobBoards jb left join fetch jb.job j " +
            "order by b.createdAt DESC")
    Page<Board> findAll(Pageable pageable);

//    @Query("select b from board b join fetch b.user u left join fetch b.image i left join fetch b.tag t " +
//            "where b.title like concat(:keyword, '%') or b.content like concat(:keyword, '%') " +
//            "or b.id=(select t2.board.id from tag t2 where t2.category like concat(:keyword, '%')) " +
//            "order by b.createdAt desc")
   // Page<Board> searchWithKeyword(@Param("keyword") String keyword, Pageable pageable);
}
