package com.alldriver.alldriver.community.service.impl;

import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.common.util.JwtUtils;
import com.alldriver.alldriver.community.domain.Community;
import com.alldriver.alldriver.community.domain.CommunityComment;
import com.alldriver.alldriver.community.dto.request.CommunityCommentSaveRequestDto;
import com.alldriver.alldriver.community.dto.request.CommunityCommentUpdateRequestDto;
import com.alldriver.alldriver.community.dto.response.CommunityCommentFindResponseDto;
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
    public String save(CommunityCommentSaveRequestDto communityCommentSaveRequestDto) {
        Long communityId = communityCommentSaveRequestDto.getCommunityId();
        String userId = JwtUtils.getUserId();
        Long parentId = communityCommentSaveRequestDto.getParentId();
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMUNITY_NOT_FOUND));



        if(parentId == null) {
            CommunityComment entity = communityCommentSaveRequestDto.toEntity(user, community);
            communityCommentRepository.save(entity);
        }
        else{
            CommunityComment communityComment = communityCommentRepository
                    .findById(parentId).orElseThrow(() -> new CustomException(ErrorCode.COMMUNITY_COMMENT_NOT_FOUND));
            if(communityComment.getCommunity().getId().equals(communityId)) {
                CommunityComment entity = communityCommentSaveRequestDto.toEntity(user, community, communityComment);
                communityCommentRepository.save(entity);
            }
            else{
                throw new CustomException(ErrorCode.INVALID_COMMENT, " 커뮤니티와 댓글이 일치하지 않습니다.");
            }
        }

        return "댓글 작성 완료.";
    }



    @Override
    public String delete(Long id) {
        CommunityComment communityComment = communityCommentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMUNITY_COMMENT_NOT_FOUND));
        communityComment.setDeleted(true);
        communityCommentRepository.save(communityComment);

        return "댓글 삭제 완료.";
    }

    @Override
    public String update(CommunityCommentUpdateRequestDto communityCommentUpdateRequestDto) {
        Long id = communityCommentUpdateRequestDto.getId();

        CommunityComment communityComment = communityCommentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMUNITY_COMMENT_NOT_FOUND));

        communityComment.updateComment(communityCommentUpdateRequestDto);
        communityCommentRepository.save(communityComment);
        return "댓글 수정 완료.";
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommunityCommentFindResponseDto> findCommentByCommunityId(Long communityId) {
        Map<Long, CommunityCommentFindResponseDto> commentHashMap = new HashMap<>();
        List<CommunityCommentFindResponseDto> result = new ArrayList<>();
        List<CommunityComment> communityComments = communityCommentRepository.findByCommunityId(communityId);
        for (CommunityComment communityComment : communityComments) {
            CommunityCommentFindResponseDto dto = communityComment.convert();
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
