package com.alldriver.alldriver.community.service.impl;

import com.alldriver.alldriver.common.util.JwtUtils;
import com.alldriver.alldriver.community.dto.response.CommunityFindResponseDto;
import com.alldriver.alldriver.community.repository.CommunityRepository;
import com.alldriver.alldriver.community.service.CommunityRetrieveService;
import com.alldriver.alldriver.community.vo.CommunityFindVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class CommunityRetrieveServiceImpl implements CommunityRetrieveService {
    private final CommunityRepository communityRepository;

    @Value("${spring.data.rest.default-page-size}")
    private Integer pageSize;
    @Override
    public List<CommunityFindResponseDto> findAll(Integer page) {
        String userId = JwtUtils.getUserId();
        Integer offset = page * pageSize;

        return getCommunityFindResponseDto(communityRepository.findAll(pageSize, offset, userId));
    }

    @Override
    public List<CommunityFindResponseDto> findByUserId(Integer page) {
        String userId = JwtUtils.getUserId();
        Integer offset = page * pageSize;

        return getCommunityFindResponseDto(communityRepository.findByUserId(pageSize, offset, userId));
    }

    @Override
    public List<CommunityFindResponseDto> findBySubLocationId(Integer page, List<Long> subLocationIds) {
        String userId = JwtUtils.getUserId();
        Integer offset = page * pageSize;

        return getCommunityFindResponseDto(communityRepository.findBySubLocation(pageSize, offset, userId, subLocationIds));
    }




    private List<CommunityFindResponseDto> getCommunityFindResponseDto(List<CommunityFindVo> communityFindVos){
        List<CommunityFindResponseDto> result = new ArrayList<>();
        for (CommunityFindVo communityFindVo : communityFindVos) {
            CommunityFindResponseDto communityFindResponseDto = CommunityFindResponseDto.builder().id(communityFindVo.getId())
                    .bookmarkCount(communityFindVo.getBookmarkCount())
                    .title(communityFindVo.getTitle())
                    .content(communityFindVo.getContent())
                    .bookmarked(communityFindVo.getBookmarked())
                    .createdAt(communityFindVo.getCreatedAt())
                    .userId(communityFindVo.getUserId())
                    .nickname(communityFindVo.getNickname())
                    .build();

            if(communityFindVo.getLocationCategory() !=null) {
                String[] split = communityFindVo.getLocationCategory().split(",");
                communityFindResponseDto.getLocationCategories().addAll(Arrays.stream(split).toList());
            }

            result.add(communityFindResponseDto);
        }

        return result;
    }
}
