package com.alldriver.alldriver.chat.controller;


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
import com.alldriver.alldriver.chat.dto.response.ChatRoomFindResponseDto;
import com.alldriver.alldriver.chat.dto.response.ChatRoomSaveResponseDto;
import com.alldriver.alldriver.chat.service.ChatRoomService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name="chat room")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @PostMapping("/chat/room")
    @Operation(description = "리턴되는 room 번호로 새로운 채팅방 생성")
    public ResponseEntity<ChatRoomSaveResponseDto> createRoom(@RequestBody @Valid ChatRoomSaveRequestDto chatRoomSaveRequestDto){
        return ResponseEntity.ok(chatRoomService.createRoom(chatRoomSaveRequestDto));
    }

    @GetMapping("/chat/rooms")
    @Operation(description = "모든 채팅방 조회")
    public ResponseEntity<List<ChatRoomFindResponseDto>> findAll(@NotBlank(message = "Student Id Not Found.")
                                                                     @RequestParam(name="userId") String userId){
        return ResponseEntity.ok(chatRoomService.findByUserId(userId));
    }
    @DeleteMapping("/chat/room/delete/{roomId}")
    @Operation(description = "해당 room id로 채팅방 삭제")
    public ResponseEntity<String> deleteChat(@PathVariable
                                         @NotNull(message = "Chat Room Id Not Found.") Long roomId){
        return ResponseEntity.ok(chatRoomService.delete(roomId));
    }


}
