package com.alldriver.alldriver.board.service.impl;


import com.alldriver.alldriver.board.document.BoardDocument;
import com.alldriver.alldriver.board.domain.*;
import com.alldriver.alldriver.board.dto.query.BoardFindJpqlResponseDto;
import com.alldriver.alldriver.board.dto.query.subLocationQueryDto;
import com.alldriver.alldriver.board.dto.response.*;
import com.alldriver.alldriver.board.repository.BoardImageRepository;
import com.alldriver.alldriver.board.repository.BoardRepository;
import com.alldriver.alldriver.board.repository.BoardSearchRepository;
import com.alldriver.alldriver.board.repository.LocationBoardRepository;
import com.alldriver.alldriver.board.service.BoardRetrieveService;
import com.alldriver.alldriver.board.vo.BoardFindVo;
import com.alldriver.alldriver.board.vo.BoardJpaResponseDto;
import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.common.exception.CustomException;
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
import java.util.stream.Collectors;

import static com.alldriver.alldriver.common.configuration.MapperConfig.modelMapper;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BoardRetrieveServiceImpl implements BoardRetrieveService {
    private final BoardRepository boardRepository;
    private final BoardImageRepository boardImageRepository;
    private final BoardSearchRepository boardSearchRepository;
    private final LocationBoardRepository locationBoardRepository;
    @Value("${spring.data.rest.default-page-size}")
    private Integer pageSize;


    @Override
    public List<BoardFindJpqlResponseDto> findAllByJpql(Integer page) {
        String userId = JwtUtils.getUserId();

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<BoardJpaResponseDto> all = boardRepository.findAll(userId, pageable);
        log.info("{}", all.getTotalElements());

        return getResponse(all.stream().toList());
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
            map.getJobs().add(job.getCategory());
        }
        for (CarBoard carBoard : carBoards) {
            map.getCars().add(carBoard.getCar().getCategory());
        }
        for (BoardImage boardImage : boardImages) {
            map.getBoardImages().add(new ImageFindResponseDto(boardImage.getId(),boardImage.getUrl()));
        }
        return map;
    }

    @Override
    public List<BoardFindResponseDto> findAll(Integer page) {

        String userId = JwtUtils.getUserId();
        Integer offset = page * pageSize;
        List<BoardFindVo> findVoList = boardRepository.findAll(userId);

        return getBoardFindResponseDto(findVoList);
    }

    @Override
    public List<BoardFindResponseDto> findByCars(Integer page, List<Long> carIds) {
        String userId = JwtUtils.getUserId();
        Integer offset = page * pageSize;
        List<BoardFindVo> findVoList = boardRepository.findByCars(carIds, userId);

        return getBoardFindResponseDto(findVoList);
    }

    @Override
    public List<BoardFindResponseDto> findByJobs(Integer page, List<Long> jobIds) {
        String userId = JwtUtils.getUserId();
        Integer offset = page * pageSize;
        List<BoardFindVo> findVoList = boardRepository.findByJobs(jobIds, userId);

        return getBoardFindResponseDto(findVoList);
    }

    @Override
    public List<BoardFindResponseDto> findBySubLocations(Integer page, List<Long> subLocationIds) {
        String userId = JwtUtils.getUserId();
        Integer offset = page * pageSize;
        List<BoardFindVo> findVoList = boardRepository.findBySubLocations(subLocationIds, userId);

        return getBoardFindResponseDto(findVoList);

    }

    @Override
    public List<BoardFindResponseDto> findByMainLocation(Integer page, Long mainLocationId) {
        String userId = JwtUtils.getUserId();
        Integer offset = page * pageSize;
        List<BoardFindVo> findVoList = boardRepository.findByMainLocation(mainLocationId, userId);

        return getBoardFindResponseDto(findVoList);
    }

    @Override
    public List<BoardFindResponseDto> findByUserId(Integer page) {
        String userId = JwtUtils.getUserId();
        Integer offset = page * pageSize;
        List<BoardFindVo> findVoList = boardRepository.findByUserId(userId);

        return getBoardFindResponseDto(findVoList);
    }

    @Override
    public List<BoardFindResponseDto> search(Integer page, String keyword) {
        String userId = JwtUtils.getUserId();
        Integer offset = page * pageSize;

        List<BoardFindVo> findVoList = boardRepository.search(keyword, userId);

        return getBoardFindResponseDto(findVoList);
    }

    @Override
    public List<BoardFindResponseDto> findByComplexParameters(Integer page, List<Long> carIds, List<Long> jobIds, List<Long> subLocationIds, Long mainLocationId) {
        String userId = JwtUtils.getUserId();
        Integer offset = page * pageSize;
        List<BoardFindVo> findVoList = boardRepository.findByComplexParameters(carIds, jobIds, subLocationIds, mainLocationId, userId);

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
        List<BoardFindVo> findVoList = boardRepository.findByBookmark(userId);

        return getBoardFindResponseDto(findVoList);
    }

    @Override
    public List<BoardSearchResponseDto> searchByEs(Integer page, String keyword) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<BoardDocument> search = boardSearchRepository.search(keyword, pageable);

        return search.stream()
                .map(BoardSearchResponseDto::new)
                .collect(Collectors.toList());
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

    public List<BoardFindJpqlResponseDto> getResponse(List<BoardJpaResponseDto> boardJpaResponseDtos){

        List<BoardFindJpqlResponseDto> result = boardJpaResponseDtos.stream().map(BoardFindJpqlResponseDto::new).toList();
        List<Long> ids = result.stream().map(BoardFindJpqlResponseDto::getId).toList();

        List<subLocationQueryDto> byBoardIds = locationBoardRepository.findByBoardIds(ids);

        Map<Long, List<subLocationQueryDto>> locations = byBoardIds.stream().collect(Collectors.groupingBy(subLocationQueryDto::boardId));
        result.forEach(boardFindJpqlResponseDto -> {
            boardFindJpqlResponseDto.setSubLocations(locations.get(boardFindJpqlResponseDto.getId()));
            boardFindJpqlResponseDto.setMainLocation(new MainLocationFindResponseDto(locations.get(boardFindJpqlResponseDto.getId()).getFirst().mainLocationId(), locations.get(boardFindJpqlResponseDto.getId()).getFirst().mainLocation()));
        });
        return result;
    }
}
