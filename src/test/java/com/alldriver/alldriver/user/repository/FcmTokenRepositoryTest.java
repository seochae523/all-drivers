package com.alldriver.alldriver.user.repository;

import com.alldriver.alldriver.common.configuration.QueryDslConfig;
import com.alldriver.alldriver.common.enums.Role;
import com.alldriver.alldriver.user.domain.FcmToken;
import com.alldriver.alldriver.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryDslConfig.class})
public class FcmTokenRepositoryTest {
    @Autowired
    private FcmTokenRepository fcmTokenRepository;

    @Autowired
    private UserRepository userRepository;
    @BeforeEach
    void setUp() {

        User user = User.builder()
                .userId("test")
                .name("testName")
                .createdAt(LocalDateTime.now())
                .phoneNumber("01012345678")
                .role(Role.USER.getValue())
                .deleted(false)
                .password("testPassword")
                .build();

        FcmToken token = FcmToken.builder().token("token")
                .user(user)
                .build();
        user.addFcmToken(token);
        userRepository.save(user);
    }

    @Test

    void save(){
        // given
        FcmToken token = FcmToken.builder().user(new User()).token("test").build();

        //when
        FcmToken save = fcmTokenRepository.save(token);

        //then
        assertThat(token.getToken()).isEqualTo(save.getToken());
    }

    @Test
    void findByUserId(){
        // given
        String userId = "test";
        //when
        FcmToken token = fcmTokenRepository.findByUserId(userId).get();
        // then
        assertThat(token.getUser().getUserId()).isEqualTo(userId);
    }

    @Test
    void findByUserIdFail(){
        // given
        String userId = "XXXXX";
        //when
        boolean result = fcmTokenRepository.findByUserId(userId).isPresent();
        // then
        assertThat(result).isFalse();
    }
}