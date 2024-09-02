package com.alldriver.alldriver.user.repository;

import com.alldriver.alldriver.user.domain.UserCarInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarImageRepository extends JpaRepository<UserCarInformation, Long> {
}
