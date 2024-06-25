package com.alldriver.alldriver.user.domain;

import com.alldriver.alldriver.board.domain.Image;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="license")
public class License {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="license_number", columnDefinition = "varchar", length = 50)
    private String licenseNumber;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "license_id")
    private List<LicenseImage> licenseImage;

}
