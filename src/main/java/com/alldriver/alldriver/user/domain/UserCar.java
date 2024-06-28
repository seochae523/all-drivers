package com.alldriver.alldriver.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="user_car")
public class UserCar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="car_number", columnDefinition = "varchar", length = 10, nullable = false)
    private String carNumber;

    @Column(name="category", columnDefinition = "varchar", length = 10, nullable = false)
    private String category;

    @Column(name="weight", columnDefinition = "varchar", length = 5, nullable = false)
    private String weight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "userCar")
    private List<CarImage> carImage;
}
