package com.alldriver.alldriver.board.service.impl;

import com.alldriver.alldriver.board.document.BoardDocument;
import com.alldriver.alldriver.board.domain.*;
import com.alldriver.alldriver.board.dto.response.BoardFindResponseDto;
import com.alldriver.alldriver.board.dto.query.SubLocationQueryDto;
import com.alldriver.alldriver.board.dto.request.*;
import com.alldriver.alldriver.board.dto.response.*;
import com.alldriver.alldriver.board.repository.*;
import com.alldriver.alldriver.board.service.BoardService;

import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.common.util.JwtUtils;
import com.alldriver.alldriver.common.util.S3Utils;
import com.alldriver.alldriver.user.domain.User;
import com.alldriver.alldriver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
    private final CarBoardRepository carBoardRepository;
    private final JobBoardRepository jobBoardRepository;
    private final LocationBoardRepository locationBoardRepository;

    private final S3Utils s3Utils;
    private final BoardSearchRepository boardSearchRepository;
    @Override
    public BoardSaveResponseDto save(List<MultipartFile> images, BoardSaveRequestDto boardSaveRequestDto) throws IOException {
        String userId = JwtUtils.getUserId();
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        Long mainLocationId = boardSaveRequestDto.getMainLocationId();
        MainLocation mainLocation = mainLocationRepository.findById(mainLocationId)
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
        List<String> carDocs = cars.stream().map(Car::getCategory).toList();
        List<String> jobDocs = jobs.stream().map(Job::getCategory).toList();
        List<String> locationDocs = subLocations.stream().map(SubLocation::getCategory).toList();

        BoardDocument document = save.toDocument(carDocs, jobDocs, locationDocs, mainLocation.getCategory(), user.getUserId());
        BoardDocument saveDocument = boardSearchRepository.save(document);

        if(images != null) {
            for (MultipartFile image : images) {
                String url = s3Utils.uploadFile(image);

                BoardImage boardImage = BoardImage.builder()
                        .board(save)
                        .url(url)
                        .build();

                boardImageRepository.save(boardImage);
            }
        }

        return BoardSaveResponseDto.builder()
                .id(save.getId())
                .title(save.getTitle())
                .content(save.getContent())
                .userId(save.getUser().getUserId())
                .build();
    }

    @Override
    public BoardUpdateResponseDto update(List<MultipartFile> images, BoardUpdateRequestDto boardUpdateRequestDto) throws IOException {
        Board board = boardRepository.findById(boardUpdateRequestDto.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND, " 게시글 id = " + boardUpdateRequestDto.getId()));

        String userId = JwtUtils.getUserId();
        if(!board.getUser().getUserId().equals(userId)) throw new CustomException(ErrorCode.INVALID_USER, " 게시글 작성자와 수정자가 일치하지 않습니다. 작성자 아이디 = " + board.getUser().getUserId() + " 수정자 아이디 = " + userId);

        List<JobUpdateRequestDto> jobInfos = boardUpdateRequestDto.getJobInfos();
        List<CarUpdateRequestDto> carInfos = boardUpdateRequestDto.getCarInfos();
        List<LocationUpdateRequestDto> locationInfos = boardUpdateRequestDto.getLocationInfos();
        List<Long> imageIds = boardUpdateRequestDto.getImageIds();
        Long mainLocationId = boardUpdateRequestDto.getMainLocationId();
        Long boardId = board.getId();


        board.updateBoard(boardUpdateRequestDto);
        if(carInfos != null) {
            // 삭제를 먼저하기 위해 정렬
            carInfos.sort(Comparator.comparing(CarUpdateRequestDto::type));
            for (CarUpdateRequestDto carInfo : carInfos) {
                Car car = carRepository.findById(carInfo.id())
                        .orElseThrow(() -> new CustomException(ErrorCode.CAR_NOT_FOUND, " 차량 id = " + carInfo.id()));
                Long carId = car.getId();

                if (carInfo.type() == 0) {
                    // 추가
                    CarBoard carBoard = CarBoard.builder()
                            .car(car)
                            .board(board)
                            .build();
                    board.addCarBoard(carBoard);

                } else if (carInfo.type() == -1) {
                    // 삭제
                    CarBoard carBoard = carBoardRepository.findByBoardIdAndCarId(boardId, carId)
                            .orElseThrow(() -> new CustomException(ErrorCode.CAR_NOT_FOUND, " 차량 id = " + carInfo.id() + " 게시글 id = " + boardId));

                    carBoardRepository.delete(carBoard);
                    board.getCarBoards().remove(carBoard);
                }
            }
        }
        if(jobInfos != null) {
            // 삭제를 먼저하기 위해 정렬
            jobInfos.sort(Comparator.comparing(JobUpdateRequestDto::type));
            for (JobUpdateRequestDto jobInfo : jobInfos) {
                Job job = jobRepository.findById(jobInfo.id())
                        .orElseThrow(() -> new CustomException(ErrorCode.JOB_NOT_FOUND, " 직종 id = " + jobInfo.id()));
                Long jobId = job.getId();

                if (jobInfo.type() == 0) {
                    JobBoard jobBoard = JobBoard.builder()
                            .job(job)
                            .board(board)
                            .build();
                    board.addJobBoard(jobBoard);

                }
                else if (jobInfo.type() == -1) {
                    JobBoard jobBoard = jobBoardRepository.findByBoardIdAndJobId(boardId, jobId)
                            .orElseThrow(() -> new CustomException(ErrorCode.JOB_NOT_FOUND, " 직종 id = " + jobInfo.id() + " 게시글 id = " + boardId));

                    jobBoardRepository.delete(jobBoard);
                    board.getJobBoards().remove(jobBoard);
                }

            }
        }
        if(locationInfos != null) {
            // 삭제를 먼저하기 위해 정렬
            locationInfos.sort(Comparator.comparing(LocationUpdateRequestDto::type));
            for (LocationUpdateRequestDto locationInfo : locationInfos) {
                SubLocation subLocation = subLocationRepository.findById(locationInfo.id())
                        .orElseThrow(() -> new CustomException(ErrorCode.SUB_LOCATION_NOT_FOUND, " 상세 지역 Id = " + locationInfo.id()));
                MainLocation mainLocation = subLocation.getMainLocation();
                Long subLocationId = subLocation.getId();
                if(!mainLocation.getId().equals(mainLocationId)) throw new CustomException(ErrorCode.INVALID_LOCATION_ID, " 수정할 메인 지역 id = " + mainLocationId + " 수정할 세부 지역에 귀속된 메인 지역 id = " + mainLocation.getId());

                if (locationInfo.type() == 0) {
                    LocationBoard locationBoard = LocationBoard.builder()
                            .subLocation(subLocation)
                            .board(board)
                            .build();

                    board.addLocationBoard(locationBoard);
                } else if (locationInfo.type() == -1) {
                    LocationBoard locationBoard = locationBoardRepository.findByBoardIdAndSubLocationId(boardId, subLocationId)
                            .orElseThrow(() -> new CustomException(ErrorCode.SUB_LOCATION_NOT_FOUND, " 상세 지역 id = " + locationInfo.id() + " 게시글 id = " + boardId));

                    locationBoardRepository.delete(locationBoard);
                    board.getLocationBoards().remove(locationBoard);
                }

            }
        }
        Board save = boardRepository.save(board);

        if(imageIds!=null) {
            for (Long imageId : imageIds) {
                BoardImage boardImage = boardImageRepository.findById(imageId)
                        .orElseThrow(() -> new CustomException(ErrorCode.IMAGE_NOT_FOUND, " 이미지 id = " + imageId));

                boardImageRepository.delete(boardImage);
                s3Utils.deleteFile(boardImage.getUrl());

            }
        }
        if(images != null) {
            for (MultipartFile image : images) {
                String url = s3Utils.uploadFile(image);

                BoardImage boardImage = BoardImage.builder()
                        .board(board)
                        .url(url)
                        .build();

                boardImageRepository.save(boardImage);
            }
        }


        return BoardUpdateResponseDto.builder()
                .id(save.getId())
                .title(save.getTitle())
                .content(save.getContent())
                .build();
    }

    @Override
    public BoardDeleteResponseDto delete(Long id) {
        String userId = JwtUtils.getUserId();
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        if(!board.getUser().getUserId().equals(userId)) throw new CustomException(ErrorCode.INVALID_USER);

        board.setDeleted(true);
        Board save = boardRepository.save(board);

        return BoardDeleteResponseDto.builder()
                .id(save.getId())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PagingResponseDto<List<BoardFindResponseDto>> findByUserId(Integer page) {
        String userId = JwtUtils.getUserId();
        Pageable pageable = PageRequest.of(page, 10);

        Page<BoardFindResponseDto> result = boardRepository.findByUserId(userId, pageable);
        List<BoardFindResponseDto> response = getBoards(result.stream().toList());

        return PagingResponseDto.<List<BoardFindResponseDto>>builder()
                .totalElements(result.getTotalElements())
                .currentPage(page)
                .totalPage(result.getTotalPages()-1)
                .data(response).build();
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
