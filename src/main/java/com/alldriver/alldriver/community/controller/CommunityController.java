package com.alldriver.alldriver.community.controller;


import com.alldriver.alldriver.common.enums.ValidationError;
import com.alldriver.alldriver.community.dto.request.CommunitySaveRequestDto;
import com.alldriver.alldriver.community.dto.request.CommunityUpdateRequestDto;
import com.alldriver.alldriver.community.dto.response.CommunityDeleteResponseDto;
import com.alldriver.alldriver.community.dto.response.CommunitySaveResponseDto;
import com.alldriver.alldriver.community.dto.response.CommunityUpdateResponseDto;
import com.alldriver.alldriver.community.service.CommunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/user/community")
@Tag(name = "커뮤니티 관련 api")
@RequiredArgsConstructor
@Validated
public class CommunityController {
    private final CommunityService communityService;

    @PostMapping("/save")
    @Operation(summary = "커뮤니티 저장", description = "subLocationIds = [지역 - 시 id]")
    public ResponseEntity<CommunitySaveResponseDto> save(@RequestBody
                                                         @Valid CommunitySaveRequestDto communitySaveRequestDto){
        return ResponseEntity.ok(communityService.save(communitySaveRequestDto));
    }
    @PutMapping("/update")
    @Operation(summary = "커뮤니티 수정")
    public ResponseEntity<CommunityUpdateResponseDto> update(@RequestBody
                                                             @Valid CommunityUpdateRequestDto communitySaveRequestDto){
        return ResponseEntity.ok(communityService.update(communitySaveRequestDto));
    }
    @DeleteMapping("/delete/{communityId}")
    @Operation(summary = "커뮤니티 삭제")
    public ResponseEntity<CommunityDeleteResponseDto> delete(@PathVariable
                                                             @NotNull Long communityId){
        return ResponseEntity.ok(communityService.delete(communityId));
    }

}
