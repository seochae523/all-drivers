package com.alldriver.alldriver.community.repository;

import com.alldriver.alldriver.community.domain.CommunityBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommunityBookmarkRepository extends JpaRepository<CommunityBookmark, Long> {
    @Query("select cb from community_bookmark cb where cb.community.id=:communityId and cb.user.userId=:userId")
    Optional<CommunityBookmark> findByCommunityIdAndUserId(@Param("communityId") Long communityId, @Param("userId") String userId);
}
