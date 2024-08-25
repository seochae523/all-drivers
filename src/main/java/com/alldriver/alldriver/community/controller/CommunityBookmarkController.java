package com.alldriver.alldriver.community.controller;


import com.alldriver.alldriver.common.enums.ValidationError;
import com.alldriver.alldriver.community.dto.response.CommunityBookmarkDeleteResponseDto;
import com.alldriver.alldriver.community.dto.response.CommunityBookmarkSaveResponseDto;
import com.alldriver.alldriver.community.service.CommunityBookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/community/bookmark")
@Tag(name = "커뮤니티 즐겨찾기 관련 api")
@Validated
public class CommunityBookmarkController {
    private final CommunityBookmarkService communityBookmarkService;

    @PutMapping("/save/{communityId}")
    @Operation(summary = "커뮤니티 즐겨찾기")
    public ResponseEntity<CommunityBookmarkSaveResponseDto> save(@PathVariable
                                                                 @NotNull(message = ValidationError.Message.COMMUNITY_ID_NOT_FOUND) Long communityId){
        return ResponseEntity.ok(communityBookmarkService.save(communityId));
    }

    @DeleteMapping("/delete/{communityId}")
    @Operation(summary = "커뮤니티 즐겨찾기 삭제")
    public ResponseEntity<CommunityBookmarkDeleteResponseDto> delete(@PathVariable
                                                                     @NotNull(message = ValidationError.Message.COMMUNITY_ID_NOT_FOUND) Long communityId){
        return ResponseEntity.ok(communityBookmarkService.delete(communityId));
    }
}
