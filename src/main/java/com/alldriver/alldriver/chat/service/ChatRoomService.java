package com.alldriver.alldriver.chat.service;



import com.alldriver.alldriver.chat.dto.request.ChatRoomSaveRequestDto;
import com.alldriver.alldriver.chat.dto.response.ChatRoomFindResponseDto;
import com.alldriver.alldriver.chat.dto.response.ChatRoomSaveResponseDto;

import java.util.List;

public interface ChatRoomService {

    ChatRoomSaveResponseDto createRoom(ChatRoomSaveRequestDto chatRoomSaveRequestDto);
    String participateChatRoom(Long roomId);
    void delete(Long roomId);
    List<ChatRoomFindResponseDto> findByUserId(String userId);
}
