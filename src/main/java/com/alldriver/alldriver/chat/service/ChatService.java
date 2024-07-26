package com.alldriver.alldriver.chat.service;

import com.alldriver.alldriver.chat.dto.request.ChatSaveRequestDto;
import com.alldriver.alldriver.chat.dto.response.ChatFindResponseDto;
import com.alldriver.alldriver.chat.dto.response.ChatSaveResponseDto;

import java.util.List;

public interface ChatService {
    ChatSaveResponseDto chat(ChatSaveRequestDto chatSaveRequestDto);
    List<ChatFindResponseDto> findAll(Long roomId);

    ChatFindResponseDto findLastChat(Long roomId);
}
