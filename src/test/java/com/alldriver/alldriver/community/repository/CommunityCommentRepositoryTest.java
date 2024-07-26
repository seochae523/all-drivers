package com.alldriver.alldriver.community.repository;

import com.alldriver.alldriver.community.domain.Community;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class CommunityCommentRepositoryTest {

    @Autowired
    private CommunityCommentRepository communityCommentRepository;

    @Autowired
    private CommunityRepository communityRepository;

    @BeforeEach
    void init(){
        for(int i = 1; i< 11; i++) {
            Community community = Community.builder()
                    .id((long) i)
                    .title("test" + i)
                    .content("test content" + i)
                    .deleted(false)
                    .createdAt(LocalDateTime.now())
                    .build();

            communityRepository.save(community);
        }
    }


    @Test
    void findByCommunityId() {
    }
}