package com.alldriver.alldriver.community.service;

import com.alldriver.alldriver.community.dto.request.CommunitySaveRequestDto;
import com.alldriver.alldriver.community.dto.request.CommunityUpdateRequestDto;
import com.alldriver.alldriver.community.dto.response.CommunitySaveResponseDto;

public interface CommunityService {
    CommunitySaveResponseDto save(CommunitySaveRequestDto communitySaveRequestDto);
    String update(CommunityUpdateRequestDto communityUpdateRequestDto);
    String delete(Long communityId);

}
