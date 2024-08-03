package com.alldriver.alldriver.community.controller;


import com.alldriver.alldriver.community.dto.request.CommunitySaveRequestDto;
import com.alldriver.alldriver.community.dto.request.CommunityUpdateRequestDto;
import com.alldriver.alldriver.community.dto.response.CommunityDeleteResponseDto;
import com.alldriver.alldriver.community.dto.response.CommunitySaveResponseDto;
import com.alldriver.alldriver.community.dto.response.CommunityUpdateResponseDto;
import com.alldriver.alldriver.community.service.CommunityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/user/community")
@Tag(name = "커뮤니티 관련 api")
@RequiredArgsConstructor
public class CommunityController {
    private final CommunityService communityService;

    @PostMapping("/save")

    public ResponseEntity<CommunitySaveResponseDto> save(@RequestBody CommunitySaveRequestDto communitySaveRequestDto){
        return ResponseEntity.ok(communityService.save(communitySaveRequestDto));
    }
    @PutMapping("/update")
    public ResponseEntity<CommunityUpdateResponseDto> update(@RequestBody CommunityUpdateRequestDto communitySaveRequestDto){
        return ResponseEntity.ok(communityService.update(communitySaveRequestDto));
    }
    @DeleteMapping("/delete/{communityId}")
    public ResponseEntity<CommunityDeleteResponseDto> delete(@PathVariable Long communityId){
        return ResponseEntity.ok(communityService.delete(communityId));
    }

}
