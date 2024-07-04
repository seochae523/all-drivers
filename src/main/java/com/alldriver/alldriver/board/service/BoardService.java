package com.alldriver.alldriver.board.service;

import org.springframework.web.multipart.MultipartFile;
import com.alldriver.alldriver.board.dto.request.BoardSaveRequestDto;
import com.alldriver.alldriver.board.dto.request.BoardUpdateRequestDto;
import com.alldriver.alldriver.board.dto.response.*;

import java.io.IOException;
import java.util.List;

public interface BoardService {

    BoardSaveResponseDto save(List<MultipartFile> multipartFile, BoardSaveRequestDto boardSaveRequestDto) throws IOException;
    String update(List<MultipartFile> images, BoardUpdateRequestDto boardUpdateRequestDto) throws IOException;
    String delete(Long id, String userId);



}
