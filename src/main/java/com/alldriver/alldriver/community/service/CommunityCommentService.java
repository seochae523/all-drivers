package com.alldriver.alldriver.community.service;

import com.alldriver.alldriver.community.dto.request.CommunityCommentSaveRequestDto;
import com.alldriver.alldriver.community.dto.request.CommunityCommentUpdateRequestDto;
import com.alldriver.alldriver.community.dto.response.CommunityCommentDeleteResponseDto;
import com.alldriver.alldriver.community.dto.response.CommunityCommentFindResponseDto;
import com.alldriver.alldriver.community.dto.response.CommunityCommentSaveResponseDto;
import com.alldriver.alldriver.community.dto.response.CommunityCommentUpdateResponseDto;

import java.util.List;

public interface CommunityCommentService {
    CommunityCommentSaveResponseDto save(CommunityCommentSaveRequestDto communityCommentSaveRequestDto);

    CommunityCommentDeleteResponseDto delete(Long id);
    CommunityCommentUpdateResponseDto update(CommunityCommentUpdateRequestDto communityCommentUpdateRequestDto);
    List<CommunityCommentFindResponseDto> findCommentByCommunityId(Long communityId);
}
