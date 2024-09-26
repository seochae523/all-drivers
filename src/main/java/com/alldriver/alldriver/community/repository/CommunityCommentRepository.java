package com.alldriver.alldriver.community.repository;

import com.alldriver.alldriver.community.domain.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {
    // 부모 댓글 먼저 찾기 위한 nulls first
    @Query("select c from community_comment c left join fetch c.user u where c.community.id=:communityId order by c.parentComment.id nulls first")
    List<CommunityComment> findByCommunityId(@Param("communityId") Long communityId);
}
