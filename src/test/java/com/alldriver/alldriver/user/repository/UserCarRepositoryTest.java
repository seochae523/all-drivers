package com.alldriver.alldriver.user.repository;

import com.alldriver.alldriver.user.domain.CarImage;
import com.alldriver.alldriver.user.domain.UserCar;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UserCarRepositoryTest {

    @Autowired
    private UserCarRepository userCarRepository;

    @BeforeEach
    void init(){
        List<CarImage> carImageList = new ArrayList<>();

        CarImage testUrl1 = CarImage.builder()
                .url("testUrl1")
                .build();

        CarImage testUrl2 = CarImage.builder()
                .url("testUrl2")
                .build();

        CarImage testUrl3 = CarImage.builder()
                .url("testUrl3")
                .build();

        carImageList.add(testUrl1);
        carImageList.add(testUrl2);
        carImageList.add(testUrl3);

        UserCar userCar = UserCar.builder()
                .category("testCar")
                .weight("testWeight")
                .carNumber("testCarNumber")
                .carImage(carImageList)
                .build();

        userCarRepository.save(userCar);
    }

    @Test
    @DisplayName("조회 성공")
    void 조회_성공(){
        //given
        String carNumber = "testCarNumber";

        // when
        Optional<UserCar> carOptional = userCarRepository.findByCarNumber(carNumber);

        // then
        assertThat(carOptional).isNotEmpty();

    }

    @Test
    @DisplayName("조회 실패")
    void 조회_실패(){
        // given
        String carNumber = "wrongNumber";

        // when
        Optional<UserCar> carOptional = userCarRepository.findByCarNumber(carNumber);

        // then
        assertThat(carOptional).isEmpty();
    }

    @Test
    @DisplayName("저장")
    void 저장(){
        // given
        UserCar userCar = UserCar.builder()
                .carNumber("saveTestCarNumber")
                .weight("saveTestWeight")
                .category("saveTestCategory")
                .build();

        // when
        UserCar save = userCarRepository.save(userCar);

        // then
        assertThat(save.getCarNumber()).isEqualTo("saveTestCarNumber");
        assertThat(save.getWeight()).isEqualTo("saveTestWeight");
        assertThat(save.getCategory()).isEqualTo("saveTestCategory");
    }

}