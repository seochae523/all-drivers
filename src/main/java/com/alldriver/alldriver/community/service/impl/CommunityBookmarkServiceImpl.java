package com.alldriver.alldriver.community.service.impl;

import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.common.util.JwtUtils;
import com.alldriver.alldriver.community.domain.Community;
import com.alldriver.alldriver.community.domain.CommunityBookmark;
import com.alldriver.alldriver.community.dto.response.CommunityBookmarkDeleteResponseDto;
import com.alldriver.alldriver.community.dto.response.CommunityBookmarkSaveResponseDto;
import com.alldriver.alldriver.community.repository.CommunityBookmarkRepository;
import com.alldriver.alldriver.community.repository.CommunityRepository;
import com.alldriver.alldriver.community.service.CommunityBookmarkService;
import com.alldriver.alldriver.user.domain.User;
import com.alldriver.alldriver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CommunityBookmarkServiceImpl implements CommunityBookmarkService {
    private final CommunityBookmarkRepository communityBookmarkRepository;
    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;


    @Override
    public CommunityBookmarkSaveResponseDto save(Long communityId) {
        String userId = JwtUtils.getUserId();
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMUNITY_NOT_FOUND));

        communityBookmarkRepository.findByCommunityIdAndUserId(communityId, userId)
                .ifPresent(x->{
                    throw new CustomException(ErrorCode.DUPLICATED_BOOKMARK);
                });

        CommunityBookmark communityBookmark = CommunityBookmark.builder()
                .community(community)
                .user(user)
                .build();

        CommunityBookmark save = communityBookmarkRepository.save(communityBookmark);

        return CommunityBookmarkSaveResponseDto.builder()
                .bookmarkId(save.getId())
                .communityId(community.getId())
                .userId(user.getUserId())
                .build();
    }

    @Override
    public CommunityBookmarkDeleteResponseDto delete(Long communityId) {
        String userId = JwtUtils.getUserId();
        CommunityBookmark communityBookmark = communityBookmarkRepository.findByCommunityIdAndUserId(communityId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMUNITY_BOOKMARK_NOT_FOUND));

        communityBookmarkRepository.delete(communityBookmark);

        return CommunityBookmarkDeleteResponseDto.builder()
                .id(communityBookmark.getId())
                .build();
    }
}
