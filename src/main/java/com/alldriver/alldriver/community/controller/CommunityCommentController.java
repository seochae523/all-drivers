package com.alldriver.alldriver.community.controller;

import com.alldriver.alldriver.common.enums.ValidationError;
import com.alldriver.alldriver.community.dto.request.CommunityCommentSaveRequestDto;
import com.alldriver.alldriver.community.dto.request.CommunityCommentUpdateRequestDto;
import com.alldriver.alldriver.community.dto.response.*;
import com.alldriver.alldriver.community.service.CommunityCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/community/comment")
@RequiredArgsConstructor
@Tag(name="커뮤니티 댓글 관련 api")
@Validated
public class CommunityCommentController {
    private final CommunityCommentService communityCommentService;

    @PostMapping("/save")
    @Operation(description = "communityId = 커뮤니티 id, parentId = 답글 작성시 부모 댓글의 id. - 답글이 아닌 댓글은 없어도 됩니다.")
    public ResponseEntity<CommunityCommentSaveResponseDto> save(@RequestBody
                                                                @Valid CommunityCommentSaveRequestDto communitySaveRequestDto){
        return ResponseEntity.ok(communityCommentService.save(communitySaveRequestDto));
    }

    @PutMapping("/update")
    public ResponseEntity<CommunityCommentUpdateResponseDto> update(@RequestBody
                                                                    @Valid CommunityCommentUpdateRequestDto communityCommentUpdateRequestDto){
        return ResponseEntity.ok(communityCommentService.update(communityCommentUpdateRequestDto));
    }
    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<CommunityCommentDeleteResponseDto> delete(@PathVariable
                                                                    @NotNull(message = ValidationError.Message.COMMENT_ID_NOT_FOUND) Long commentId){
        return ResponseEntity.ok(communityCommentService.delete(commentId));
    }

    @GetMapping("/comments/{communityId}")
    public ResponseEntity<List<CommunityCommentFindResponseDto>> findCommentsByCommunityId(@PathVariable
                                                                                           @NotNull(message = ValidationError.Message.COMMUNITY_ID_NOT_FOUND) Long communityId){
        return ResponseEntity.ok(communityCommentService.findCommentByCommunityId(communityId));
    }
}
