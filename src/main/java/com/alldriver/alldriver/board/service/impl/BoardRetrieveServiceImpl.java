package com.alldriver.alldriver.board.service.impl;


import com.alldriver.alldriver.board.domain.*;
import com.alldriver.alldriver.board.dto.response.*;
import com.alldriver.alldriver.board.repository.BoardImageRepository;
import com.alldriver.alldriver.board.repository.BoardRepository;
import com.alldriver.alldriver.board.service.BoardRetrieveService;
import com.alldriver.alldriver.board.vo.BoardFindVo;
import com.alldriver.alldriver.common.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BoardRetrieveServiceImpl implements BoardRetrieveService {
    private final BoardRepository boardRepository;
    private final BoardImageRepository boardImageRepository;
    @Value("${spring.data.rest.default-page-size}")
    private Integer pageSize;




    @Override
    public List<BoardFindResponseDto> findAll(Integer page) {
        String userId = JwtUtils.getUserId();
        Integer offset = page * pageSize;
        List<BoardFindVo> findVoList = boardRepository.findAll(pageSize, offset, userId);

        return getBoardFindResponseDto(findVoList);
    }

    @Override
    public List<BoardFindResponseDto> findByCars(Integer page, List<Long> carIds) {
        String userId = JwtUtils.getUserId();
        Integer offset = page * pageSize;
        List<BoardFindVo> findVoList = boardRepository.findByCars(pageSize, offset, carIds, userId);

        return getBoardFindResponseDto(findVoList);
    }

    @Override
    public List<BoardFindResponseDto> findByJobs(Integer page, List<Long> jobIds) {
        String userId = JwtUtils.getUserId();
        Integer offset = page * pageSize;
        List<BoardFindVo> findVoList = boardRepository.findByJobs(pageSize, offset, jobIds, userId);

        return getBoardFindResponseDto(findVoList);
    }

    @Override
    public List<BoardFindResponseDto> findBySubLocations(Integer page, List<Long> subLocationIds) {
        String userId = JwtUtils.getUserId();
        Integer offset = page * pageSize;
        List<BoardFindVo> findVoList = boardRepository.findBySubLocations(pageSize, offset, subLocationIds, userId);

        return getBoardFindResponseDto(findVoList);

    }

    @Override
    public List<BoardFindResponseDto> findByMainLocation(Integer page, Long mainLocationId) {
        String userId = JwtUtils.getUserId();
        Integer offset = page * pageSize;
        List<BoardFindVo> findVoList = boardRepository.findByMainLocation(pageSize, offset, mainLocationId, userId);

        return getBoardFindResponseDto(findVoList);
    }

    @Override
    public List<BoardFindResponseDto> findByUserId(Integer page) {
        String userId = JwtUtils.getUserId();
        Integer offset = page * pageSize;
        List<BoardFindVo> findVoList = boardRepository.findByUserId(pageSize, offset, userId);

        return getBoardFindResponseDto(findVoList);
    }

    @Override
    public List<BoardFindResponseDto> search(Integer page, String keyword) {
        String userId = JwtUtils.getUserId();
        Integer offset = page * pageSize;
        List<BoardFindVo> findVoList = boardRepository.search(pageSize, offset, keyword, userId);

        return getBoardFindResponseDto(findVoList);
    }

    @Override
    public List<BoardFindResponseDto> findByComplexParameters(Integer page, List<Long> carIds, List<Long> jobIds, List<Long> subLocationIds, Long mainLocationId) {
        String userId = JwtUtils.getUserId();
        Integer offset = page * pageSize;
        List<BoardFindVo> findVoList = boardRepository.findByComplexParameters(pageSize, offset, carIds, jobIds, subLocationIds, mainLocationId, userId);

        return getBoardFindResponseDto(findVoList);
    }

    @Override
    public List<ImageFindResponseDto> findImageByBoardId(Long boardId) {
        List<ImageFindResponseDto> result = new ArrayList<>();
        List<BoardImage> images = boardImageRepository.findByBoardId(boardId);
        for (BoardImage image : images) {
            result.add(new ImageFindResponseDto(image.getId(), image.getUrl()));
        }
        return result;
    }

    @Override
    public List<BoardFindResponseDto> findMyBookmarkedBoard(Integer page) {
        String userId = JwtUtils.getUserId();
        Integer offset = page * pageSize;
        List<BoardFindVo> findVoList = boardRepository.findByBookmark(pageSize, offset, userId);

        return getBoardFindResponseDto(findVoList);
    }

    private List<BoardFindResponseDto> getBoardFindResponseDto(List<BoardFindVo> findVoList){
        Map<Long, BoardFindResponseDto> boardMap = new LinkedHashMap<>();

        for (BoardFindVo boardFindVo : findVoList) {
            Long boardId = boardFindVo.getBoardId();

            BoardFindResponseDto boardDto = boardMap.computeIfAbsent(boardId, id -> {
                BoardFindResponseDto dto = new BoardFindResponseDto();
                dto.setId(boardFindVo.getBoardId());
                dto.setCategory(boardFindVo.getCategory());
                dto.setContent(boardFindVo.getContent());
                dto.setTitle(boardFindVo.getTitle());
                dto.setUserId(boardFindVo.getUserId());
                dto.setCreatedAt(boardFindVo.getCreatedAt());
                dto.setPayment(boardFindVo.getPayment());
                dto.setPayType(boardFindVo.getPayType());
                dto.setCompanyLocation(boardFindVo.getCompanyLocation());
                dto.setRecruitType(boardFindVo.getRecruitType());
                dto.setStartAt(boardFindVo.getStartAt());
                dto.setEndAt(boardFindVo.getEndAt());
                dto.setMainLocation(boardFindVo.getMainLocation());
                dto.setUserNickname(boardFindVo.getUserNickname());
                dto.setBookmarkCount(boardFindVo.getBookmarkCount());
                dto.setBookmarked(boardFindVo.getBookmarked());
                return dto;
            });

            if (boardFindVo.getCarCategory() != null) {
                String[] split = boardFindVo.getCarCategory().split(",");

                boardDto.getCars().addAll(Arrays.stream(split).toList());
            }

            if (boardFindVo.getJobCategory() != null) {
                String[] split = boardFindVo.getJobCategory().split(",");
                boardDto.getJobs().addAll(Arrays.stream(split).toList());
            }

            if (boardFindVo.getLocationCategory() != null) {
                String[] split = boardFindVo.getLocationCategory().split(",");
                boardDto.getLocations().addAll(Arrays.stream(split).toList());
            }
        }
        return new ArrayList<>(boardMap.values());
    }
}
