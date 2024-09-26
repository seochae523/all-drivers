package com.alldriver.alldriver.community.repository;

import com.alldriver.alldriver.board.domain.SubLocation;
import com.alldriver.alldriver.board.repository.SubLocationRepository;
import com.alldriver.alldriver.common.enums.Role;
import com.alldriver.alldriver.community.domain.Community;
import com.alldriver.alldriver.community.domain.CommunityLocation;
import com.alldriver.alldriver.community.vo.CommunityFindVo;
import com.alldriver.alldriver.user.domain.User;
import com.alldriver.alldriver.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

class CommunityRepositoryTest {

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubLocationRepository subLocationRepository;
    @BeforeEach
    void init(){
        SubLocation subLocation = subLocationRepository.findById(1L).get();
        User user = User.builder()
                .userId("test")
                .name("testName")
                .createdAt(LocalDateTime.now())
                .phoneNumber("01012345678")
                .role(Role.USER.getValue())
                .deleted(false)
                .password("testPassword")
                .build();
        User save = userRepository.save(user);

        for(int i = 1; i< 11; i++) {
            Community community = Community.builder()
                    .title("test" + i)
                    .content("test content" + i)
                    .category("test")
                    .deleted(false)
                    .createdAt(LocalDateTime.now())
                    .user(save)
                    .build();
            CommunityLocation communityLocation = CommunityLocation.builder().subLocation(subLocation).community(community).build();
            community.addSubLocation(communityLocation);
            communityRepository.save(community);
        }
    }


    @Test
    @DisplayName("전체 조회")
    void findAll() {
        // given
        String userId ="test";
        // when
        List<CommunityFindVo> all = communityRepository.findAll(10, 0, userId);
        // then
        assertThat(all).hasSize(10);
    }

    @Test
    @DisplayName("저장")
    void save(){
        // given
        Community community = Community.builder()
                .title("tesTitle")
                .content("testContent")
                .category("category")
                .deleted(false)
                .build();
        // when
        Community save = communityRepository.save(community);

        // then
        assertThat(save).isInstanceOf(Community.class);
        assertThat(save.getTitle()).isEqualTo(community.getTitle());
        assertThat(save.getContent()).isEqualTo(community.getContent());
    }

    @Test
    @DisplayName("유저 이름에 따른 커뮤니티 조회 - 존재하는 유저")
    void findByUserId() {
        String userId ="test";
        // when
        List<CommunityFindVo> all = communityRepository.findByUserId(10, 0, userId);
        // then
        assertThat(all).hasSize(10);
    }
    @Test
    @DisplayName("유저 이름에 따른 커뮤니티 조회 - 존재하지 않는 유저")
    void findByUserIdIfNotExist() {
        String userId ="notExist";
        // when
        List<CommunityFindVo> all = communityRepository.findByUserId(10, 0, userId);
        // then
        assertThat(all).isEmpty();
    }

    @Test
    @DisplayName("지역에 따른 조회 - 지역 존재")
    void findBySubLocation() {
        //given
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        String userId ="test";

        //when
        List<CommunityFindVo> bySubLocation = communityRepository.findBySubLocation(10, 0, userId, ids);
        //then
        assertThat(bySubLocation).hasSize(10);
    }
    @Test
    @DisplayName("지역에 따른 조회 - 지역 미존재")
    void findBySubLocationIfNotExist() {
        //given
        List<Long> ids = new ArrayList<>();
        ids.add(2L);
        String userId ="test";

        //when
        List<CommunityFindVo> bySubLocation = communityRepository.findBySubLocation(10, 0, userId, ids);
        //then
        assertThat(bySubLocation).isEmpty();
    }
}