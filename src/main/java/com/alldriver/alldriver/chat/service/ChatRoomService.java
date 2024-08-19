package com.alldriver.alldriver.chat.service;



import com.alldriver.alldriver.chat.dto.request.ChatRoomSaveRequestDto;
import com.alldriver.alldriver.chat.dto.response.*;

import java.util.List;

public interface ChatRoomService {

    ChatRoomSaveResponseDto createRoom(ChatRoomSaveRequestDto chatRoomSaveRequestDto);
    ChatRoomParticipateResponseDto participateChatRoom(Long roomId);
    ChatRoomDeleteResponseDto delete(Long roomId);
    List<ChatRoomFindResponseDto> findByUserId();

    ChatRoomFindDetailResponseDto findChatRoomDetail(Long roomId);
}
