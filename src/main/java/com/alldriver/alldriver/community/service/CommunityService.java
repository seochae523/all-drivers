package com.alldriver.alldriver.community.service;

import com.alldriver.alldriver.community.dto.request.CommunitySaveRequestDto;
import com.alldriver.alldriver.community.dto.request.CommunityUpdateRequestDto;
import com.alldriver.alldriver.community.dto.response.CommunityDeleteResponseDto;
import com.alldriver.alldriver.community.dto.response.CommunitySaveResponseDto;
import com.alldriver.alldriver.community.dto.response.CommunityUpdateResponseDto;

public interface CommunityService {
    CommunitySaveResponseDto save(CommunitySaveRequestDto communitySaveRequestDto);
    CommunityUpdateResponseDto update(CommunityUpdateRequestDto communityUpdateRequestDto);
    CommunityDeleteResponseDto delete(Long communityId);

}
