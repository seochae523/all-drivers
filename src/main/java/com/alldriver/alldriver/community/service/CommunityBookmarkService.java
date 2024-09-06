package com.alldriver.alldriver.community.service;

import com.alldriver.alldriver.community.dto.response.CommunityBookmarkDeleteResponseDto;
import com.alldriver.alldriver.community.dto.response.CommunityBookmarkSaveResponseDto;

public interface CommunityBookmarkService {
    CommunityBookmarkSaveResponseDto save(Long communityId);
    CommunityBookmarkDeleteResponseDto delete(Long communityId);
}
