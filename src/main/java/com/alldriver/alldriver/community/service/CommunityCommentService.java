package com.alldriver.alldriver.community.service;

import com.alldriver.alldriver.community.dto.request.CommunityCommentSaveRequestDto;
import com.alldriver.alldriver.community.dto.request.CommunityCommentUpdateRequestDto;
import com.alldriver.alldriver.community.dto.response.CommunityCommentFindResponseDto;

import java.util.List;

public interface CommunityCommentService {
    String save(CommunityCommentSaveRequestDto communityCommentSaveRequestDto);

    String delete(Long id);
    String update(CommunityCommentUpdateRequestDto communityCommentUpdateRequestDto);
    List<CommunityCommentFindResponseDto> findCommentByCommunityId(Long communityId);
}
