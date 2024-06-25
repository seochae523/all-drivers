package com.alldriver.alldriver.board.service.impl;

import com.alldriver.alldriver.board.domain.Board;
import com.alldriver.alldriver.board.dto.request.BoardSaveRequestDto;
import com.alldriver.alldriver.board.dto.request.BoardUpdateRequestDto;
import com.alldriver.alldriver.board.dto.response.BoardDeleteResponseDto;
import com.alldriver.alldriver.board.dto.response.BoardFindResponseDto;
import com.alldriver.alldriver.board.dto.response.BoardSaveResponseDto;
import com.alldriver.alldriver.board.dto.response.BoardUpdateResponseDto;
import com.alldriver.alldriver.board.repository.BoardRepository;
import com.alldriver.alldriver.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;

    @Value("${spring.data.rest.default-page-size}")
    private Integer pageSize;
    @Override
    public List<BoardFindResponseDto> findAll(Integer page) {
         Pageable pageable = PageRequest.of(page, pageSize);

        Page<Board> result = boardRepository.findAll(pageable);

        return result.stream()
                .filter(x -> !x.getDeleted())
                .map(BoardFindResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public BoardSaveResponseDto save(List<MultipartFile> multipartFile, BoardSaveRequestDto boardSaveRequestDto) throws IOException {
        return null;
    }

    @Override
    public BoardUpdateResponseDto update(BoardUpdateRequestDto boardUpdateRequestDto) {
        return null;
    }

    @Override
    public BoardDeleteResponseDto delete(Long id, String email) {
        return null;
    }

    @Override
    public List<BoardFindResponseDto> search(String keyword, Integer page) {
        return null;
    }
}
