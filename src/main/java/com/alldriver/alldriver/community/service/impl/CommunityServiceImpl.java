package com.alldriver.alldriver.community.service.impl;

import com.alldriver.alldriver.board.domain.SubLocation;
import com.alldriver.alldriver.board.repository.SubLocationRepository;
import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.common.util.JwtUtils;
import com.alldriver.alldriver.community.domain.Community;
import com.alldriver.alldriver.community.domain.CommunityLocation;
import com.alldriver.alldriver.community.dto.request.CommunitySaveRequestDto;
import com.alldriver.alldriver.community.dto.request.CommunityUpdateRequestDto;
import com.alldriver.alldriver.community.dto.response.CommunityDeleteResponseDto;
import com.alldriver.alldriver.community.dto.response.CommunitySaveResponseDto;
import com.alldriver.alldriver.community.dto.response.CommunityUpdateResponseDto;
import com.alldriver.alldriver.community.repository.CommunityRepository;
import com.alldriver.alldriver.community.service.CommunityService;
import com.alldriver.alldriver.user.domain.User;
import com.alldriver.alldriver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommunityServiceImpl implements CommunityService {
    private final CommunityRepository communityRepository;
    private final SubLocationRepository subLocationRepository;
    private final UserRepository userRepository;

    @Override
    public CommunitySaveResponseDto save(CommunitySaveRequestDto communitySaveRequestDto) {
        List<Long> subLocationIds = communitySaveRequestDto.getSubLocationIds();
        String userId = JwtUtils.getUserId();

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND, " 사용자 아이디 = " + userId));

        List<SubLocation> subLocations = subLocationRepository.findByIds(subLocationIds);
        Community community = communitySaveRequestDto.toEntity(user);
        for (SubLocation subLocation : subLocations) {
            CommunityLocation communityLocation = CommunityLocation.builder()
                    .subLocation(subLocation)
                    .build();

            community.addSubLocation(communityLocation);
        }

        Community save = communityRepository.save(community);


        return CommunitySaveResponseDto.builder()
                .id(save.getId())
                .title(save.getTitle())
                .content(save.getContent())
                .createdAt(save.getCreatedAt())
                .build();
    }

    @Override
    public CommunityUpdateResponseDto update(CommunityUpdateRequestDto communityUpdateRequestDto) {
        Long id = communityUpdateRequestDto.getId();
        Community community = communityRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMUNITY_NOT_FOUND));

        community.update(communityUpdateRequestDto);

        Community save = communityRepository.save(community);

        return CommunityUpdateResponseDto.builder()
                .id(save.getId())
                .title(save.getTitle())
                .content(save.getContent())
                .build();
    }

    @Override
    public CommunityDeleteResponseDto delete(Long communityId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMUNITY_NOT_FOUND));

        community.setDeleted(true);
        Community save = communityRepository.save(community);

        return CommunityDeleteResponseDto.builder()
                .id(save.getId())
                .build();
    }
}
