package com.alldriver.alldriver.community.controller;


import com.alldriver.alldriver.community.dto.response.CommunityFindResponseDto;
import com.alldriver.alldriver.community.service.CommunityRetrieveService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/community")
@Tag(name = "커뮤니티 조회 관련 api")
public class CommunityRetrieveController {
    private final CommunityRetrieveService communityRetrieveService;

    @GetMapping("/all")
    public ResponseEntity<List<CommunityFindResponseDto>> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page){
        return ResponseEntity.ok(communityRetrieveService.findAll(page));
    }

    @GetMapping("/userId")
    public ResponseEntity<List<CommunityFindResponseDto>> findByUserId(@RequestParam(value = "page", defaultValue = "0") Integer page){
        return ResponseEntity.ok(communityRetrieveService.findByUserId(page));
    }

    @GetMapping("/subLocation")
    public ResponseEntity<List<CommunityFindResponseDto>> findBySubLocationId(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                              @RequestParam(value = "subLocationId", required = false) Long subLocationId){
        return ResponseEntity.ok(communityRetrieveService.findBySubLocationId(page, subLocationId));
    }


}
