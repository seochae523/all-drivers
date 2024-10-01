package com.alldriver.alldriver.community.controller;


import com.alldriver.alldriver.common.enums.ValidationError;
import com.alldriver.alldriver.community.dto.response.CommunityFindResponseDto;
import com.alldriver.alldriver.community.service.CommunityRetrieveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/community")
@Tag(name = "커뮤니티 조회 관련 api")
@Validated
public class CommunityRetrieveController {
    private final CommunityRetrieveService communityRetrieveService;

    @GetMapping("/all/{page}")
    @Operation(summary = "커뮤니티를 10개씩 조회 (페이지 시작 0부터)")
    public ResponseEntity<List<CommunityFindResponseDto>> findAll(@Min(value = 0, message = ValidationError.Message.MINIMUM_PAGE_VALUE_ERROR)
                                                                  @PathVariable Integer page){
        return ResponseEntity.ok(communityRetrieveService.findAll(page));
    }

    @GetMapping("/userId/{page}")
    @Operation(summary = "커뮤니티를 유저 id로 10개씩 조회 (페이지 시작 0부터) - 유저 id는 token 삽입시 자동 추출")
    public ResponseEntity<List<CommunityFindResponseDto>> findByUserId(@Min(value = 0, message = ValidationError.Message.MINIMUM_PAGE_VALUE_ERROR)
                                                                           @PathVariable Integer page){
        return ResponseEntity.ok(communityRetrieveService.findByUserId(page));
    }

    @GetMapping("/subLocation/{page}")
    @Operation(summary = "커뮤니티를 [지역 - 구 id]로 조회")
    public ResponseEntity<List<CommunityFindResponseDto>> findBySubLocationId(@Min(value = 0, message = ValidationError.Message.MINIMUM_PAGE_VALUE_ERROR)
                                                                              @PathVariable Integer page,
                                                                              @RequestParam(value = "subLocationId", required = false)
                                                                              @NotNull List<Long> subLocationIds){
        return ResponseEntity.ok(communityRetrieveService.findBySubLocationId(page, subLocationIds));
    }


}
