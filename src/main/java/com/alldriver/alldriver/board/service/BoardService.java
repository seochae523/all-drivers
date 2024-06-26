package com.alldriver.alldriver.board.service;

import org.springframework.web.multipart.MultipartFile;
import com.alldriver.alldriver.board.dto.request.BoardSaveRequestDto;
import com.alldriver.alldriver.board.dto.request.BoardUpdateRequestDto;
import com.alldriver.alldriver.board.dto.response.*;

import java.io.IOException;
import java.util.List;

public interface BoardService {
    List<BoardFindResponseDto> findAll(Integer page);
    BoardSaveResponseDto save(List<MultipartFile> multipartFile, BoardSaveRequestDto boardSaveRequestDto) throws IOException;
    BoardUpdateResponseDto update(BoardUpdateRequestDto boardUpdateRequestDto);
    BoardDeleteResponseDto delete(Long id, String email);
    List<BoardFindResponseDto> search(String keyword, Integer page);
}
