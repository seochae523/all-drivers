package com.alldriver.alldriver.community.repository;

import com.alldriver.alldriver.common.configuration.QueryDslConfig;
import com.alldriver.alldriver.community.domain.Community;

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
class CommunityCommentRepositoryTest {

    @Autowired
    private CommunityRepository communityRepository;

    @BeforeEach
    void init(){
        for(int i = 1; i< 11; i++) {
            Community community = Community.builder()
                    .title("test" + i)
                    .content("test content" + i)
                    .category("test")
                    .deleted(false)
                    .createdAt(LocalDateTime.now())
                    .build();

            communityRepository.save(community);
        }
    }


    @Test
    @DisplayName("커뮤니티 id로 조회")
    void findByCommunityId() {
        // given
        Community community = Community.builder()
                .title("test")
                .content("test content")
                .category("test")
                .deleted(false)
                .createdAt(LocalDateTime.now())
                .build();
        System.out.println("community123 = " + community.getCategory());
        Community save = communityRepository.save(community);

        // when
        Optional<Community> result = communityRepository.findById(save.getId());

        // then
        assertThat(result).isNotEmpty();
    }
}