package com.alldriver.alldriver.community.repository;


import com.alldriver.alldriver.community.domain.Community;
import com.alldriver.alldriver.community.vo.CommunityFindVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {
    String BASE_QUERY = "select c.id as id, c.title as title, c.content as content, c.created_at as createdAt, " +
            "group_concat(distinct s.category) as locationCategory, " +
            "(select count(*) from community_bookmark cb1 where cb1.community_id=c.id) as bookmarkCount, " +
            "u.user_id as userId, " +
            "case when cb.user_id is not null then true else false end as bookmarked " +
            "from community as c " +
            "left join community_bookmark cb on cb.community_id=c.id and cb.user_id = (select id from user u1 where u1.user_id=:userId) " +
            "left join user u on u.id=c.user_id " +
            "left join community_location cs on cs.community_id=c.id "+
            "left join sub_location s on s.id=cs.location_id " +
            "where c.deleted=false ";

    String SORT_QUERY = "group by c.id, c.title, c.content, c.created_at, bookmarked " +
            "order by bookmarked desc, createdAt desc ";

    @Query(value = BASE_QUERY +
            SORT_QUERY +
            "limit :limit offset :offset", nativeQuery = true)
    List<CommunityFindVo> findAll(@Param("limit") int limit, @Param("offset") int offset, @Param("userId") String userId);

    @Query(value = BASE_QUERY +
            "and u.user_id=:userId " +
            SORT_QUERY +
            "limit :limit offset :offset", nativeQuery = true)
    List<CommunityFindVo> findByUserId(@Param("limit") int limit, @Param("offset") int offset, @Param("userId") String userId);

    @Query(value = BASE_QUERY +
            "and s.id in :subLocationIds " +
            SORT_QUERY +
            "limit :limit offset :offset", nativeQuery = true)
    List<CommunityFindVo> findBySubLocation(@Param("limit") int limit, @Param("offset") int offset, @Param("userId") String userId, @Param("subLocationIds") List<Long> subLocationIds);
}
