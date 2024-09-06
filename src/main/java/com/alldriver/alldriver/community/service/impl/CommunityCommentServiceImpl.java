package com.alldriver.alldriver.community.service.impl;

import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.common.util.JwtUtils;
import com.alldriver.alldriver.community.domain.Community;
import com.alldriver.alldriver.community.domain.CommunityComment;
import com.alldriver.alldriver.community.dto.request.CommunityCommentSaveRequestDto;
import com.alldriver.alldriver.community.dto.request.CommunityCommentUpdateRequestDto;
import com.alldriver.alldriver.community.dto.response.CommunityCommentDeleteResponseDto;
import com.alldriver.alldriver.community.dto.response.CommunityCommentFindResponseDto;
import com.alldriver.alldriver.community.dto.response.CommunityCommentSaveResponseDto;
import com.alldriver.alldriver.community.dto.response.CommunityCommentUpdateResponseDto;
import com.alldriver.alldriver.community.repository.CommunityCommentRepository;
import com.alldriver.alldriver.community.repository.CommunityRepository;
import com.alldriver.alldriver.community.service.CommunityCommentService;
import com.alldriver.alldriver.user.domain.User;
import com.alldriver.alldriver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CommunityCommentServiceImpl implements CommunityCommentService {
    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    private final CommunityCommentRepository communityCommentRepository;

    @Override
    public CommunityCommentSaveResponseDto save(CommunityCommentSaveRequestDto communityCommentSaveRequestDto) {
        Long communityId = communityCommentSaveRequestDto.getCommunityId();
        String userId = JwtUtils.getUserId();
        Long parentId = communityCommentSaveRequestDto.getParentId();

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMUNITY_NOT_FOUND));

        CommunityComment save;


        if(parentId == null) {
            CommunityComment entity = communityCommentSaveRequestDto.toEntity(user, community);
            save = communityCommentRepository.save(entity);
        }

        else{
            CommunityComment communityComment = communityCommentRepository
                    .findById(parentId).orElseThrow(() -> new CustomException(ErrorCode.COMMUNITY_COMMENT_NOT_FOUND));

            if(communityComment.getCommunity().getId().equals(communityId)) {
                CommunityComment entity = communityCommentSaveRequestDto.toEntity(user, community, communityComment);
                save = communityCommentRepository.save(entity);
            }

            else{
                throw new CustomException(ErrorCode.INVALID_COMMENT, " 커뮤니티가 일치하지 않습니다.");
            }

        }

        return CommunityCommentSaveResponseDto.builder()
                .id(save.getId())
                .content(save.getContent())
                .communityId(community.getId())
                .build();
    }



    @Override
    public CommunityCommentDeleteResponseDto delete(Long id) {
        CommunityComment communityComment = communityCommentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMUNITY_COMMENT_NOT_FOUND));
        communityComment.setDeleted(true);

        CommunityComment save = communityCommentRepository.save(communityComment);

        return CommunityCommentDeleteResponseDto.builder()
                .id(save.getId())
                .build();
    }

    @Override
    public CommunityCommentUpdateResponseDto update(CommunityCommentUpdateRequestDto communityCommentUpdateRequestDto) {
        Long id = communityCommentUpdateRequestDto.getId();

        CommunityComment communityComment = communityCommentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMUNITY_COMMENT_NOT_FOUND));

        communityComment.updateComment(communityCommentUpdateRequestDto);
        CommunityComment save = communityCommentRepository.save(communityComment);

        return CommunityCommentUpdateResponseDto.builder()
                .id(save.getId())
                .content(save.getContent())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommunityCommentFindResponseDto> findCommentByCommunityId(Long communityId) {
        Map<Long, CommunityCommentFindResponseDto> commentHashMap = new HashMap<>();
        List<CommunityCommentFindResponseDto> result = new ArrayList<>();
        List<CommunityComment> communityComments = communityCommentRepository.findByCommunityId(communityId);
        for (CommunityComment communityComment : communityComments) {
            String nickname = communityComment.getUser().getNickname();
            CommunityCommentFindResponseDto dto = communityComment.convert(nickname);
            commentHashMap.put(communityComment.getId(), dto);
            if(communityComment.getParentComment()==null){
                result.add(dto);
            }
            else{
                commentHashMap.get(communityComment.getParentComment().getId()).getChildren().add(dto);
            }
        }
        return result;
    }
}
