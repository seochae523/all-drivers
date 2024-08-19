package com.alldriver.alldriver.chat.controller;


import com.alldriver.alldriver.chat.dto.response.*;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.alldriver.alldriver.chat.dto.request.ChatRoomSaveRequestDto;
import com.alldriver.alldriver.chat.service.ChatRoomService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name="채팅방 관련 api")
@Hidden
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @PostMapping("/chat/room")
    @Operation(description = "리턴되는 room 번호로 새로운 채팅방 생성")
    public ResponseEntity<ChatRoomSaveResponseDto> createRoom(@RequestBody @Valid ChatRoomSaveRequestDto chatRoomSaveRequestDto){
        return ResponseEntity.ok(chatRoomService.createRoom(chatRoomSaveRequestDto));
    }

    @GetMapping("/chat/rooms")
    @Operation(description = "모든 채팅방 조회")
    public ResponseEntity<List<ChatRoomFindResponseDto>> findAll(){
        return ResponseEntity.ok(chatRoomService.findByUserId());
    }
    @DeleteMapping("/chat/room/delete/{roomId}")
    @Operation(description = "해당 room id로 채팅방 삭제")
    public ResponseEntity<ChatRoomDeleteResponseDto> deleteChat(@PathVariable @NotNull(message = "채팅방 id가 존재하지 않습니다.") Long roomId){
        return ResponseEntity.ok(chatRoomService.delete(roomId));
    }

    @GetMapping("/chat/room/detail/{roomId}")
    @Operation(description = "해당 room id로 채팅방 디테일 조회")
    public ResponseEntity<ChatRoomFindDetailResponseDto> findChatRoomDetail(@PathVariable @NotNull(message = "채팅방 id가 존재하지 않습니다.") Long roomId){
        return ResponseEntity.ok(chatRoomService.findChatRoomDetail(roomId));
    }

    @PostMapping("/chat/room/participate/{roomId}")
    @Operation(description = "해당 room id로 채팅방 가입")
    public ResponseEntity<ChatRoomParticipateResponseDto> participateChatRoom(@PathVariable @NotNull(message = "채팅방 id가 존재하지 않습니다.") Long roomId){
        return ResponseEntity.ok(chatRoomService.participateChatRoom(roomId));
    }
}
