//package com.alldriver.alldriver.chat.controller;
//
//
//import io.swagger.v3.oas.annotations.Hidden;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.validation.Valid;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Positive;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RestController;
//import com.alldriver.alldriver.chat.dto.request.ChatSaveRequestDto;
//import com.alldriver.alldriver.chat.dto.response.ChatFindResponseDto;
//import com.alldriver.alldriver.chat.service.ChatService;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//@Validated
//@Tag(name = "채팅 관련 api")
//@Hidden
//public class ChatController {
//
//    private final ChatService chatService;
//
//    // /pub/message
//    @MessageMapping("/message")
//    public void sendMessage(@Valid ChatSaveRequestDto chatSaveRequestDto){
//        chatService.chat(chatSaveRequestDto);
//    }
//
//    @GetMapping("/user/chat/{roomId}")
//    @Operation(description = "해당 room id의 채팅 조회")
//    public ResponseEntity<List<ChatFindResponseDto>> findAllChat(@PathVariable @NotNull(message = "채팅방 id가 존재하지 않습니다.") Long roomId){
//        return ResponseEntity.ok(chatService.findAll(roomId));
//    }
//
//    @GetMapping("/user/chat/last/{roomId}")
//    @Operation(description = "해당 room id의 채팅 조회")
//    public ResponseEntity<ChatFindResponseDto> findLastChat(@PathVariable @NotNull(message = "채팅방 id가 존재하지 않습니다.") Long roomId){
//        return ResponseEntity.ok(chatService.findLastChat(roomId));
//    }
//
//}
