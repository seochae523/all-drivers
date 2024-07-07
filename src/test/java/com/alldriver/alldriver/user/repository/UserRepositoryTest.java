package com.alldriver.alldriver.user.repository;


import com.alldriver.alldriver.common.emun.Role;
import com.alldriver.alldriver.user.domain.User;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;


import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void init(){
            User user = User.builder()
                    .userId("test")
                    .name("testName")
                    .createdAt(LocalDateTime.now())
                    .phoneNumber("01012345678")
                    .nickname("test")
                    .role(Role.USER.getValue())
                    .deleted(false)
                    .password("testPassword")
                    .build();


            userRepository.save(user);
    }


    @Test
    @DisplayName("올바른 전화번호 조회")
    void 올바른_전화번호_조회(){
        // given
        String correctPhoneNumber = "01012345678";

        // when
        Optional<User> correct = userRepository.findByPhoneNumber(correctPhoneNumber);

        // then
        assertThat(correct).isNotEmpty();

    }

    @Test
    @DisplayName("틀린 전화번호 조회")
    void 틀린_전화번호_조회(){
        // given
        String wrongPhoneNumber = "01012341234";

        // when
        Optional<User> wrong = userRepository.findByPhoneNumber(wrongPhoneNumber);

        // then
        assertThat(wrong).isEmpty();
    }

    @Test
    @DisplayName("유저 아이디 따른 조회")
    void 올바른_유저_아이디에_따른_조회(){
        // given
        String correctUserId = "test";

        // when
        Optional<User> correct = userRepository.findByUserId(correctUserId);

        // then
        assertThat(correct).isNotEmpty();
    }

    @Test
    @DisplayName("ID에 따른 조회")
    void 틀린_유저_아이디에_따른_조회(){
        // given

        String wrongUserId = "wrong";

        // when

        Optional<User> wrong = userRepository.findByUserId(wrongUserId);

        // then

        assertThat(wrong).isEmpty();
    }

    @Test
    @DisplayName("올바른 닉네임 조회")
    void 올바른_닉네임_조회(){
        // given
        String correctNickname = "test";


        // when
        Optional<User> correct = userRepository.findByNickname(correctNickname);


        // then
        assertThat(correct).isNotEmpty();

    }
    @Test
    @DisplayName("틀린 닉네임 조회")
    void 틀린_닉네임_조회(){
        // given
        String wrongNickname = "wrong";

        // when

        Optional<User> wrong = userRepository.findByNickname(wrongNickname);

        // then

        assertThat(wrong).isEmpty();
    }

    @Test
    @DisplayName("저장")
    void 저장(){
        // given
        User user = User.builder()
                .userId("save")
                .name("save")
                .createdAt(LocalDateTime.now())
                .phoneNumber("01012345678")
                .nickname("save")
                .role(Role.USER.getValue())
                .deleted(false)
                .password("save")
                .build();

        // when
        User save = userRepository.save(user);

        // then
        assertThat(save).isEqualTo(user);
    }
}
