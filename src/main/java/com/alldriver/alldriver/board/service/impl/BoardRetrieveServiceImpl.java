package com.alldriver.alldriver.board.service.impl;

import com.alldriver.alldriver.board.domain.Board;
import com.alldriver.alldriver.board.dto.response.BoardFindResponseDto;
import com.alldriver.alldriver.board.repository.BoardRepository;
import com.alldriver.alldriver.board.service.BoardRetrieveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BoardRetrieveServiceImpl implements BoardRetrieveService {
    private final BoardRepository boardRepository;
    @Value("${spring.data.rest.default-page-size}")
    private Integer pageSize;

    /**
     * TODO : 메서드 중복 처리 방법 고안하기
     */
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
    public List<BoardFindResponseDto> findByCars(Integer page, List<Long> carIds) {
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Board> result = boardRepository.findByCars(pageable, carIds);

        return result.stream()
                .filter(x -> !x.getDeleted())
                .map(BoardFindResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BoardFindResponseDto> findByJobs(Integer page, List<Long> jobIds) {
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Board> result = boardRepository.findByJobs(pageable, jobIds);

        return result.stream()
                .filter(x -> !x.getDeleted())
                .map(BoardFindResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<BoardFindResponseDto> findBySubLocations(Integer page, List<Long> subLocationIds) {
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Board> result = boardRepository.findBySubLocations(pageable, subLocationIds);

        return result.stream()
                .filter(x -> !x.getDeleted())
                .map(BoardFindResponseDto::new)
                .collect(Collectors.toList());

    }

    @Override
    public List<BoardFindResponseDto> findByMainLocation(Integer page, Long mainLocationId) {
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Board> result = boardRepository.findByMainLocation(pageable, mainLocationId);

        return result.stream()
                .filter(x -> !x.getDeleted())
                .map(BoardFindResponseDto::new)
                .collect(Collectors.toList());
    }
}
