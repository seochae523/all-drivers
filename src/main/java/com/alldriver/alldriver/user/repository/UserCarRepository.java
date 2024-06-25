package com.alldriver.alldriver.user.repository;

import com.alldriver.alldriver.user.domain.UserCar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCarRepository extends JpaRepository<UserCar, Long> {
    @Query("select c from UserCar c where c.carNumber=:carNumber")
    Optional<UserCar> findByCarNumber(@Param("carNumber") String carNumber);
}
