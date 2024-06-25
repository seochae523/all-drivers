package com.alldriver.alldriver.user.repository;

import com.alldriver.alldriver.user.domain.LicenseImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LicenseImageRepository extends JpaRepository<LicenseImage, Long> {
}
