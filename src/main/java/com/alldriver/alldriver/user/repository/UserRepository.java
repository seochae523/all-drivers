package com.alldriver.alldriver.user.repository;

import com.alldriver.alldriver.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.phoneNumber=:phoneNumber and u.deleted=false")
    Optional<User> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);
    @Query("select u from User u where u.userId=:userId and u.deleted=false")
    Optional<User> findByUserId(@Param("userId") String userId);
    @Query("select u from User u where u.nickname=:nickname and u.deleted=false")
    Optional<User> findByNickname(@Param("nickname") String nickname);
    @Query("select u from User u left join fetch UserCar c where c.carNumber=:carNumber")
    Optional<User> findByCarNumber(@Param("carNumber") String carNumber);
}
