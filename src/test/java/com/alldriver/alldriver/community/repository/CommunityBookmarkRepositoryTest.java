package com.alldriver.alldriver.community.repository;

import com.alldriver.alldriver.common.enums.Role;
import com.alldriver.alldriver.community.domain.Community;
import com.alldriver.alldriver.community.domain.CommunityBookmark;
import com.alldriver.alldriver.user.domain.User;
import com.alldriver.alldriver.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

class CommunityBookmarkRepositoryTest {

    @Autowired
    private CommunityBookmarkRepository communityBookmarkRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommunityRepository communityRepository;

    @BeforeEach
    void init(){

    }

    @Test
    @DisplayName("좋아요 조회")
    void findByCommunityIdAndUserId() {
        // given
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

        Community community = Community.builder()
                .title("test")
                .content("test content")
                .deleted(false)
                .createdAt(LocalDateTime.now())
                .build();

        User userSave = userRepository.save(user);
        Community communitySave = communityRepository.save(community);

        CommunityBookmark build = CommunityBookmark.builder().user(userSave).community(communitySave).build();
        CommunityBookmark save = communityBookmarkRepository.save(build);

        // when
        Optional<CommunityBookmark> result = communityBookmarkRepository.findByCommunityIdAndUserId(communitySave.getId(), userSave.getUserId());


        // then
        assertThat(result).isNotEmpty();
    }


}