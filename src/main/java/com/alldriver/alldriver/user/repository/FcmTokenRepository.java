package com.alldriver.alldriver.user.repository;

import com.alldriver.alldriver.user.domain.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {

    @Query("select t from FcmToken t left join fetch t.user u where u.userId=:userId")
    Optional<FcmToken> findByUserId(@Param("userId") String userId);
}
