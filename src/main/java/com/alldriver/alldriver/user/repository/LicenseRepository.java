package com.alldriver.alldriver.user.repository;

import com.alldriver.alldriver.user.domain.CompanyInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LicenseRepository extends JpaRepository<CompanyInformation, Long> {

    @Query("select l from CompanyInformation l where l.licenseNumber=:licenseNumber")
    Optional<CompanyInformation> findByLicenseNumber(@Param("licenseNumber") String licenseNumber);

}
