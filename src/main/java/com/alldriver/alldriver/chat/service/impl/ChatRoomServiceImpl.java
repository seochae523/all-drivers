package com.alldriver.alldriver.chat.service.impl;

import com.alldriver.alldriver.chat.dto.request.ChatRoomSaveRequestDto;
import com.alldriver.alldriver.chat.dto.response.ChatRoomFindResponseDto;
import com.alldriver.alldriver.chat.dto.response.ChatRoomSaveResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alldriver.alldriver.board.domain.Board;
import com.alldriver.alldriver.board.repository.BoardRepository;

import com.alldriver.alldriver.chat.repository.ChatRoomRepository;
import com.alldriver.alldriver.chat.service.ChatRoomService;

import com.alldriver.alldriver.user.repository.UserRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ChatRoomServiceImpl implements ChatRoomService {
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public ChatRoomSaveResponseDto createRoom(ChatRoomSaveRequestDto chatRoomSaveRequestDto) {
        return null;
    }

    @Override
    public String participateChatRoom(Long roomId) {
        return null;
    }

    @Override
    public void delete(Long roomId) {

    }

    @Override
    public List<ChatRoomFindResponseDto> findByUserId(String userId) {
        return null;
    }
}
