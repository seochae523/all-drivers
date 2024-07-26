package com.alldriver.alldriver.community.controller;

import com.alldriver.alldriver.community.domain.CommunityBookmark;
import com.alldriver.alldriver.community.service.CommunityBookmarkService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/community/bookmark")
@Tag(name = "커뮤니티 즐겨찾기 관련 api")
public class CommunityBookmarkController {
    private final CommunityBookmarkService communityBookmarkService;

    @PostMapping("/save/{communityId}")
    public ResponseEntity<String> save(@PathVariable Long communityId){
        return ResponseEntity.ok(communityBookmarkService.save(communityId));
    }

    @DeleteMapping("/delete/{communityId}")
    public ResponseEntity<String> delete(@PathVariable Long communityId){
        return ResponseEntity.ok(communityBookmarkService.delete(communityId));
    }
}
