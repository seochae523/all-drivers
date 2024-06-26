package com.alldriver.alldriver.user.repository;

import com.alldriver.alldriver.user.domain.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LicenseRepository extends JpaRepository<License, Long> {

    @Query("select l from License l where l.licenseNumber=:licenseNumber")
    Optional<License> findByLicenseNumber(@Param("licenseNumber") String licenseNumber);

}
