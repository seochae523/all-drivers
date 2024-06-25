package com.alldriver.alldriver.user.repository;

import com.alldriver.alldriver.user.domain.SmsSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SmsRepository extends JpaRepository<SmsSession, Long> {

    Optional<SmsSession> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);
}

