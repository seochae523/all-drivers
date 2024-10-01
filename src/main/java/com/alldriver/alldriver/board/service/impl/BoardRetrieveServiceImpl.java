package com.alldriver.alldriver.board.service.impl;


import com.alldriver.alldriver.board.document.BoardDocument;
import com.alldriver.alldriver.board.domain.*;
import com.alldriver.alldriver.board.dto.response.BoardFindResponseDto;
import com.alldriver.alldriver.board.dto.query.BoardSearchCondition;
import com.alldriver.alldriver.board.dto.query.SubLocationQueryDto;
import com.alldriver.alldriver.board.dto.response.*;

import com.alldriver.alldriver.board.repository.BoardRepository;
import com.alldriver.alldriver.board.repository.BoardSearchRepository;
import com.alldriver.alldriver.board.repository.LocationBoardRepository;
import com.alldriver.alldriver.board.service.BoardRetrieveService;

import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.common.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.alldriver.alldriver.common.configuration.MapperConfig.modelMapper;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BoardRetrieveServiceImpl implements BoardRetrieveService {
    private final BoardRepository boardRepository;

    private final BoardSearchRepository boardSearchRepository;
    private final LocationBoardRepository locationBoardRepository;
    @Value("${spring.data.rest.default-page-size}")
    private Integer pageSize;


    @Override
    public PagingResponseDto<List<BoardFindResponseDto>> findAll(Integer page) {
        String userId = JwtUtils.getUserId();

        Page<BoardFindResponseDto> result = boardRepository.findAll(userId, PageRequest.of(page, pageSize));
        List<BoardFindResponseDto> response = getBoards(result.stream().toList());

        return PagingResponseDto.<List<BoardFindResponseDto>>builder()
                .currentPage(page)
                .totalElements(result.getTotalElements())
                .totalPage(result.getTotalPages()-1)
                .data(response)
                .build();
    }


    @Override
    public BoardDetailResponseDto findDetailById(Long id) {
        Board board = boardRepository.findDetailById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        log.info("{}", board.getId());
        BoardDetailResponseDto map = modelMapper.map(board, BoardDetailResponseDto.class);

        Set<CarBoard> carBoards = board.getCarBoards();
        Set<JobBoard> jobBoards = board.getJobBoards();
        Set<BoardImage> boardImages = board.getBoardImages();

        for (JobBoard jobBoard : jobBoards) {
            Job job = jobBoard.getJob();
            map.getJobs().add(new JobFindResponseDto(job.getId(), job.getCategory()));
        }
        for (CarBoard carBoard : carBoards) {
            Car car = carBoard.getCar();
            map.getCars().add(new CarFindResponseDto(car.getId(), car.getCategory()));
        }
        for (BoardImage boardImage : boardImages) {
            map.getBoardImages().add(new ImageFindResponseDto(boardImage.getId(),boardImage.getUrl()));
        }
        return map;
    }



    @Override
    public PagingResponseDto<List<BoardFindResponseDto>> findMyBookmarkedBoard(Integer page) {
        String userId = JwtUtils.getUserId();

        Page<BoardFindResponseDto> result = boardRepository.findMyBookmarkedBoard(userId, PageRequest.of(page, pageSize));
        List<BoardFindResponseDto> response = getBoards(result.stream().toList());

        return PagingResponseDto.<List<BoardFindResponseDto>>builder()
                .currentPage(page)
                .totalElements(result.getTotalElements())
                .totalPage(result.getTotalPages()-1)
                .data(response)
                .build();
    }

    @Override
    public PagingResponseDto<List<BoardFindResponseDto>> findBy(Integer page, List<Long> jobIds, List<Long> carIds, List<Long> subLocationIds, Long mainLocationId) {
        String userId = JwtUtils.getUserId();
        BoardSearchCondition boardSearchCondition = getBoardCondition(jobIds, carIds, subLocationIds, mainLocationId);

        Page<BoardFindResponseDto> result = boardRepository.search(boardSearchCondition, PageRequest.of(page, pageSize), userId);
        List<BoardFindResponseDto> response = getBoards(result.stream().toList());

        return PagingResponseDto.<List<BoardFindResponseDto>>builder()
                .currentPage(page)
                .totalElements(result.getTotalElements())
                .totalPage(result.getTotalPages()-1)
                .data(response)
                .build();
    }

    private BoardSearchCondition getBoardCondition(List<Long> jobIds, List<Long> carIds, List<Long> subLocationIds, Long mainLocationId) {
        return BoardSearchCondition.builder().
                jobIds(jobIds).
                carIds(carIds).
                subLocationIds(subLocationIds).
                mainLocationId(mainLocationId).
                build();
    }

    @Override
    public List<BoardSearchResponseDto> search(Integer page, String keyword) {
        Page<BoardDocument> search = boardSearchRepository.search(keyword, PageRequest.of(page, pageSize));

        return search.stream()
                .map(BoardSearchResponseDto::new)
                .collect(Collectors.toList());
    }

    private List<BoardFindResponseDto> getBoards(List<BoardFindResponseDto> result){
        return setLocations(result);
    }

    private List<BoardFindResponseDto> setLocations(List<BoardFindResponseDto> result) {
        Map<Long, List<SubLocationQueryDto>> locations = findLocations(toSubLocationIds(result));
        result.forEach(boardFindJpqlResponseDto -> {
            boardFindJpqlResponseDto.setSubLocations(locations.get(boardFindJpqlResponseDto.getId()));
            boardFindJpqlResponseDto.setMainLocation(new MainLocationFindResponseDto(locations.get(boardFindJpqlResponseDto.getId()).get(0).mainLocationId(), locations.get(boardFindJpqlResponseDto.getId()).get(0).mainLocation()));
        });
        return result;
    }

    @Transactional(readOnly = true)
    public Map<Long, List<SubLocationQueryDto>> findLocations(List<Long> subLocationIds){
        List<SubLocationQueryDto> subLocations = locationBoardRepository.findByBoardIds(subLocationIds);
        return subLocations.stream().
                collect(Collectors.groupingBy(SubLocationQueryDto::boardId));
    }

    private List<Long> toSubLocationIds(List<BoardFindResponseDto> result){
        return result.stream().
                map(BoardFindResponseDto::getId).
                toList();
    }
}
