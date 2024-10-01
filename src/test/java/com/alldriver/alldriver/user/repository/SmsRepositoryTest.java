package com.alldriver.alldriver.user.repository;

import com.alldriver.alldriver.common.configuration.QueryDslConfig;
import com.alldriver.alldriver.user.domain.SmsSession;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QueryDslConfig.class})
public class SmsRepositoryTest {

    @Autowired
    private SmsRepository smsRepository;

    @BeforeEach
    void init(){
        SmsSession build = SmsSession.builder()
                .createdAt(LocalDateTime.now())
                .phoneNumber("01012345678")
                .authCode("123456")
                .build();

        smsRepository.save(build);
    }


    @Test
    @DisplayName("올바른 전화번호 조회")
    void 올바른_전화번호_조회(){
        // given
        String correctPhoneNumber = "01012345678";
        String wrongPhoneNumber = "01011112222";

        // when
        Optional<SmsSession> correct = smsRepository.findByPhoneNumber(correctPhoneNumber);
        Optional<SmsSession> wrong = smsRepository.findByPhoneNumber(wrongPhoneNumber);

        // then
        assertThat(correct).isNotEmpty();
        assertThat(wrong).isEmpty();
    }
    @Test
    @DisplayName("틀린 전화번호 조회")
    void 틀린_전화번호_조회(){
        // given
        String wrongPhoneNumber = "01011112222";

        // when
        Optional<SmsSession> wrong = smsRepository.findByPhoneNumber(wrongPhoneNumber);

        // then
        assertThat(wrong).isEmpty();
    }

    @Test
    @DisplayName("저장")
    void 저장(){
        // given
        SmsSession build = SmsSession.builder()
                .authCode("111111")
                .phoneNumber("01011112222")
                .createdAt(LocalDateTime.now())
                .build();


        // when
        SmsSession save = smsRepository.save(build);
        // then

        assertThat(build).isEqualTo(save);
    }


    @Test
    @DisplayName("삭제")
    void 삭제(){
        // given
        Optional<SmsSession> result = smsRepository.findByPhoneNumber("01012345678");

        // when
        smsRepository.delete(result.get());

        // then
        assertThat(smsRepository.findByPhoneNumber("01012345678")).isEmpty();
    }
}
