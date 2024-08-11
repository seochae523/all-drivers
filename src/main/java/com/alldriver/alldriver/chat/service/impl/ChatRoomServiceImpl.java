//package com.alldriver.alldriver.chat.service.impl;
//
//import com.alldriver.alldriver.chat.domain.ChatRoom;
//import com.alldriver.alldriver.chat.dto.request.ChatRoomSaveRequestDto;
//import com.alldriver.alldriver.chat.dto.response.ChatRoomFindResponseDto;
//import com.alldriver.alldriver.chat.dto.response.ChatRoomSaveResponseDto;
//import com.alldriver.alldriver.common.enums.ErrorCode;
//import com.alldriver.alldriver.common.exception.CustomException;
//import com.alldriver.alldriver.common.util.JwtUtils;
//import com.alldriver.alldriver.user.domain.User;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import com.alldriver.alldriver.board.domain.Board;
//import com.alldriver.alldriver.board.repository.BoardRepository;
//
//import com.alldriver.alldriver.chat.repository.ChatRoomRepository;
//import com.alldriver.alldriver.chat.service.ChatRoomService;
//
//import com.alldriver.alldriver.user.repository.UserRepository;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//@Transactional
//public class ChatRoomServiceImpl implements ChatRoomService {
//    private final UserRepository userRepository;
//    private final ChatRoomRepository chatRoomRepository;
//
//    @Override
//    public ChatRoomSaveResponseDto createRoom(ChatRoomSaveRequestDto chatRoomSaveRequestDto) {
//        LocalDateTime now = LocalDateTime.now();
//        String userId = JwtUtils.getUserId();
//        User user = userRepository.findByUserId(userId)
//                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
//
//        ChatRoom chatRoom= ChatRoom.builder()
//                .creator(user)
//                .createdAt(now)
//                .build();
//
//        ChatRoom save = chatRoomRepository.save(chatRoom);
//
//        return ChatRoomSaveResponseDto.builder()
//                .id(save.getId())
//                .creator(userId)
//                .title(save.getTitle())
//                .createdAt(now)
//                .build();
//    }
//
//    @Override
//    public String participateChatRoom(Long roomId) {
//        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
//                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));
//
//
//        return null;
//    }
//
//    @Override
//    public String delete(Long roomId) {
//        return "";
//    }
//
//    @Override
//    public List<ChatRoomFindResponseDto> findByUserId(String userId) {
//        return null;
//    }
//}
