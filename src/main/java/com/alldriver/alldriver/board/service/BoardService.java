package com.alldriver.alldriver.board.service;

import com.alldriver.alldriver.board.dto.response.BoardFindResponseDto;
import org.springframework.web.multipart.MultipartFile;
import com.alldriver.alldriver.board.dto.request.BoardSaveRequestDto;
import com.alldriver.alldriver.board.dto.request.BoardUpdateRequestDto;
import com.alldriver.alldriver.board.dto.response.*;

import java.io.IOException;
import java.util.List;

public interface BoardService {

    BoardSaveResponseDto save(List<MultipartFile> multipartFile, BoardSaveRequestDto boardSaveRequestDto) throws IOException;
    BoardUpdateResponseDto update(List<MultipartFile> images, BoardUpdateRequestDto boardUpdateRequestDto) throws IOException;
    BoardDeleteResponseDto delete(Long id);

    PagingResponseDto<List<BoardFindResponseDto>> findByUserId(Integer page);


}
