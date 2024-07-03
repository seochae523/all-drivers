package com.alldriver.alldriver.board.service.impl;

import com.alldriver.alldriver.board.domain.*;
import com.alldriver.alldriver.board.dto.request.BoardSaveRequestDto;
import com.alldriver.alldriver.board.dto.request.BoardUpdateRequestDto;
import com.alldriver.alldriver.board.dto.response.*;
import com.alldriver.alldriver.board.repository.*;
import com.alldriver.alldriver.board.service.BoardService;
import com.alldriver.alldriver.common.emun.ErrorCode;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.common.util.S3Utils;
import com.alldriver.alldriver.user.domain.User;
import com.alldriver.alldriver.user.repository.UserRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BoardServiceImpl implements BoardService {
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    private final JobRepository jobRepository;
    private final SubLocationRepository subLocationRepository;
    private final CarRepository carRepository;
    private final BoardImageRepository boardImageRepository;
    private final MainLocationRepository mainLocationRepository;
    private final S3Utils s3Utils;


    @Override
    public BoardSaveResponseDto save(List<MultipartFile> images, BoardSaveRequestDto boardSaveRequestDto) throws IOException {
        String userId = boardSaveRequestDto.getUserId();
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        Long mainLocationId = boardSaveRequestDto.getMainLocationId();
        mainLocationRepository.findById(mainLocationId)
                .orElseThrow(() -> new CustomException(ErrorCode.MAIN_LOCATION_NOT_FOUND));

        List<Long> jobNames = boardSaveRequestDto.getJob();
        List<Long> carNames = boardSaveRequestDto.getCar();
        List<Long> subLocationNames = boardSaveRequestDto.getSubLocations();

        List<Job> jobs = jobRepository.findByIds(jobNames);
        List<Car> cars = carRepository.findByIds(carNames);
        List<SubLocation> subLocations = subLocationRepository.findByIds(subLocationNames);

        Board board = boardSaveRequestDto.toEntity(user);

        for (Job job : jobs) {
            JobBoard jobBoard = JobBoard.builder()
                    .job(job)
                    .build();
            board.addJobBoard(jobBoard);
        }
        for (Car car : cars) {
            CarBoard carBoard = CarBoard.builder()
                    .car(car)
                    .build();
            board.addCarBoard(carBoard);
        }
        for (SubLocation subLocation : subLocations) {
            LocationBoard locationBoard = LocationBoard.builder()
                    .subLocation(subLocation)
                    .build();
            board.addLocationBoard(locationBoard);
        }
        Board save = boardRepository.save(board);

        for (MultipartFile image : images) {
            String url = s3Utils.uploadFile(image);

            BoardImage boardImage = BoardImage.builder()
                    .board(save)
                    .url(url)
                    .build();

            boardImageRepository.save(boardImage);
        }


        return BoardSaveResponseDto.builder()
                .title(save.getTitle())
                .content(save.getContent())
                .userId(save.getUser().getUserId())
                .build();
    }

    @Override
    public BoardUpdateResponseDto update(BoardUpdateRequestDto boardUpdateRequestDto) {
        Long id = boardUpdateRequestDto.getId();
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));


        return null;
    }

    @Override
    public BoardDeleteResponseDto delete(Long id, String email) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoardFindResponseDto> search(String keyword, Integer page) {
        return null;
    }


}
