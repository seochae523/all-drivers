package com.alldriver.alldriver.chat.repository;

import com.alldriver.alldriver.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;


@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("select r from ChatRoom r left join fetch r.creator p left join fetch r.participant s " +
            "where (s.userId=:userId or p.userId=:userId) and r.deleted=false")
    List<ChatRoom> findByUserId(@Param("userId") String userId);


}
