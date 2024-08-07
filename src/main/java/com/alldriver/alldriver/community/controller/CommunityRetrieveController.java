package com.alldriver.alldriver.community.controller;


import com.alldriver.alldriver.common.enums.ValidationError;
import com.alldriver.alldriver.community.dto.response.CommunityFindResponseDto;
import com.alldriver.alldriver.community.service.CommunityRetrieveService;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @GetMapping("/all")
    public ResponseEntity<List<CommunityFindResponseDto>> findAll(@RequestParam(value = "page", defaultValue = "0")
                                                                  @NotNull(message = ValidationError.Message.PAGE_NOT_FOUND) Integer page){
        return ResponseEntity.ok(communityRetrieveService.findAll(page));
    }

    @GetMapping("/userId")
    public ResponseEntity<List<CommunityFindResponseDto>> findByUserId(@RequestParam(value = "page", defaultValue = "0")
                                                                       @NotNull(message = ValidationError.Message.PAGE_NOT_FOUND) Integer page){
        return ResponseEntity.ok(communityRetrieveService.findByUserId(page));
    }

    @GetMapping("/subLocation")
    public ResponseEntity<List<CommunityFindResponseDto>> findBySubLocationId(@RequestParam(value = "page", defaultValue = "0")
                                                                              @NotNull(message = ValidationError.Message.PAGE_NOT_FOUND) Integer page,
                                                                              @RequestParam(value = "subLocationId")
                                                                              @NotNull(message = ValidationError.Message.SUB_LOCATION_ID_NOT_FOUND) Long subLocationId){
        return ResponseEntity.ok(communityRetrieveService.findBySubLocationId(page, subLocationId));
    }


}
