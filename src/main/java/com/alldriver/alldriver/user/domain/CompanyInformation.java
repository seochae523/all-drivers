package com.alldriver.alldriver.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.checkerframework.checker.units.qual.C;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="company_information")
public class CompanyInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="license_number", columnDefinition = "varchar", length = 50, nullable = false)
    private String licenseNumber;

    @Column(name="image", columnDefinition = "TEXT")
    private String image;

    @Column(name="company_location", columnDefinition = "varchar", length = 100, nullable = false)
    private String companyLocation;

    @Column(name="start_at", columnDefinition = "date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    @Setter
    private User user;

}
