package com.alldriver.alldriver.community.service;

import com.alldriver.alldriver.community.dto.response.CommunityFindResponseDto;

import java.util.List;

public interface CommunityRetrieveService {
    List<CommunityFindResponseDto> findAll(Integer page);

    List<CommunityFindResponseDto> findByUserId(Integer page);

    List<CommunityFindResponseDto> findBySubLocationId(Integer page, List<Long> subLocationIds);


}
