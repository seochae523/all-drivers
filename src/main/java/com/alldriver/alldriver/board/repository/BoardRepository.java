package com.alldriver.alldriver.board.repository;

import com.alldriver.alldriver.board.vo.BoardJpaResponseDto;
import com.alldriver.alldriver.board.vo.BoardFindVo;
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

    // 공통된 기본 쿼리
    String BASE_QUERY = "select b.id as boardId, b.title as title, b.category as category, b.content as content, b.company_location as companyLocation, b.created_at as createdAt, " +
            "        b.start_at as startAt, b.end_at as endAt, b.pay_type as payType, b.payment as payment, b.recruit_type as recruitType, " +
            "        ml.category as mainLocation, " +
            "        group_concat(distinct c.category) as carCategory, " +
            "        group_concat(distinct j.category) as jobCategory, " +
            "        group_concat(distinct sl.category) as locationCategory, " +
            "        u.user_id as userId, " +
            "        case when bm.user_id is not null then true else false end as bookmarked "+
            "    from board b " +
            "    left join user u on u.id=b.user_id " +
            "    left join car_board cb on b.id=cb.board_id " +
            "    left join car c on c.id=cb.car_id " +
            "    left join job_board jb on b.id=jb.board_id " +
            "    left join job j on j.id=jb.job_id " +
            "    left join location_board lb on b.id=lb.board_id " +
            "    left join sub_location sl on sl.id=lb.location_id " +
            "    left join main_location ml on ml.id=sl.main_location_id " +
            "    left join board_bookmark bm on bm.board_id=b.id and bm.user_id = (select id from user u1 where u1.user_id=:userId) "+
            "    where b.deleted=false ";

    // 정렬 쿼리
    String SORT_QUERY = "group by b.id, b.title, b.category, b.content, b.company_location, b.created_at, b.start_at, b.end_at, b.pay_type, b.payment, b.recruit_type, u.user_id, ml.category, bookmarked " +
                        "order by bookmarked desc, b.created_at desc ";

    // 전체 검색
    @Query(value = BASE_QUERY +
            SORT_QUERY +
            "limit :limit offset :offset", nativeQuery = true)
    List<BoardFindVo> findAll(@Param("userId") String userId);

    @Query("select b.id as boardId, b.title as title, b.createdAt as createdAt, u.userId as userId,  (case when bu.userId=:userId then 1 else 0 end) as bookmarked from board b " +
            "left join b.user u "+
            "left join b.boardBookmarks bm "+
            "left join bm.user bu "+
            "where b.deleted=false " +
            "order by (case when bu.userId=:userId then 1 else 0 end) desc, b.createdAt desc")
    Page<BoardJpaResponseDto> findAll(@Param("userId") String userId, Pageable pageable);

    @Query("select b from board b " +
            "left join fetch b.boardImages i " +
            "left join fetch b.carBoards cb " +
            "left join fetch cb.car c " +
            "left join fetch b.jobBoards jb " +
            "left join fetch jb.job j " +
            "where b.id=:id")
    Optional<Board> findDetailById(Long id);
      // Car ID에 따른 검색
    @Query("select b.id as boardId, b.title as title, b.createdAt as createdAt, u.userId as userId,  (case when bu.userId=:userId then 1 else 0 end) as bookmarked from board b " +
            "left join b.user u "+
            "left join b.boardBookmarks bm "+
            "left join bm.user bu "+
            "left join b.carBoards cb " +
            "left join cb.car c " +
            "where b.deleted=false and c.id in (:carIds) " +
            "order by (case when bu.userId=:userId then 1 else 0 end) desc, b.createdAt desc")
    List<BoardFindVo> findByCars(@Param("carIds") List<Long> carIds, @Param("userId") String userId);

    // Job ID에 따른 검색
    @Query("select b.id as boardId, b.title as title, b.createdAt as createdAt, u.userId as userId,  (case when bu.userId=:userId then 1 else 0 end) as bookmarked from board b " +
            "left join b.user u "+
            "left join b.boardBookmarks bm "+
            "left join bm.user bu "+
            "left join b.jobBoards jb " +
            "left join jb.job j " +
            "where b.deleted=false and j.id in (:jobIds) " +
            "order by (case when bu.userId=:userId then 1 else 0 end) desc, b.createdAt desc")
    List<BoardFindVo> findByJobs(@Param("jobIds") List<Long> jobIds, @Param("userId") String userId);

    // Sub Location ID에 따른 검색
    @Query("select b.id as boardId, b.title as title, b.createdAt as createdAt, u.userId as userId,  (case when bu.userId=:userId then 1 else 0 end) as bookmarked from board b " +
            "left join b.user u "+
            "left join b.boardBookmarks bm "+
            "left join bm.user bu "+
            "left join b.locationBoards lb "+
            "left join lb.subLocation s "+
            "where b.deleted=false and s.id in (:locationIds) " +
            "order by (case when bu.userId=:userId then 1 else 0 end) desc, b.createdAt desc")
    List<BoardFindVo> findBySubLocations(@Param("locationIds") List<Long> LocationIds, @Param("userId") String userId);

    // Main Location ID에 따른 검색
    @Query("select b.id as boardId, b.title as title, b.createdAt as createdAt, u.userId as userId,  (case when bu.userId=:userId then 1 else 0 end) as bookmarked from board b " +
            "left join b.user u "+
            "left join b.boardBookmarks bm "+
            "left join bm.user bu "+
            "left join b.locationBoards lb "+
            "left join lb.subLocation s "+
            "left join s.mainLocation ml "+
            "where b.deleted=false and ml.id=:mainLocationId " +
            "order by (case when bu.userId=:userId then 1 else 0 end) desc, b.createdAt desc")
    List<BoardFindVo> findByMainLocation(@Param("mainLocationId") Long mainLocationId, @Param("userId") String userId);

    // User ID에 따른 검색
    @Query("select b.id as boardId, b.title as title, b.createdAt as createdAt, u.userId as userId,  (case when bu.userId=:userId then 1 else 0 end) as bookmarked from board b " +
            "left join b.user u "+
            "left join b.boardBookmarks bm "+
            "left join bm.user bu "+
            "where b.deleted=false and u.userId=:userId " +
            "order by (case when bu.userId=:userId then 1 else 0 end) desc, b.createdAt desc")
    List<BoardFindVo> findByUserId(@Param("userId") String userId);

    // 키워드 검색
    @Query(value = BASE_QUERY +
            "and (title like concat('%',:keyword, '%') or " +
            "content like concat('%', :keyword, '%') or " +
            "c.category like concat('%', :keyword, '%') or " +
            "j.category like concat('%', :keyword, '%') or " +
            "sl.category like concat('%', :keyword, '%') or " +
            "ml.category like concat('%',:keyword, '%')) " +
             SORT_QUERY +
            "limit :limit offset :offset", nativeQuery = true)
    List<BoardFindVo> search(@Param("keyword") String keyword, @Param("userId") String userId);

    @Query(value = BASE_QUERY +
            "and (case when concat(:carIds) is null then true else c.id in (:carIds) end) " +
            "and (case when concat(:jobIds) is null then true else j.id in (:jobIds) end) " +
            "and (case when concat(:locationIds) is null then true else sl.id in (:locationIds) end) " +
            "and (case when (:mainLocationId) is null then true else ml.id = :mainLocationId end) " +
            SORT_QUERY +
            "limit :limit offset :offset", nativeQuery = true)
    List<BoardFindVo> findByComplexParameters( @Param("carIds") List<Long> carIds, @Param("jobIds") List<Long> jobIds, @Param("locationIds") List<Long> LocationIds, @Param("mainLocationId") Long mainLocationId, @Param("userId") String userId);


    @Query(value = BASE_QUERY +
            "and bm.user_id = (select id from user u1 where u1.user_id=:userId) " +
            SORT_QUERY +
            "limit :limit offset :offset", nativeQuery = true)
    List<BoardFindVo> findByBookmark( @Param("userId") String userId);
}
