package com.alldriver.alldriver.board.service.impl;

import com.alldriver.alldriver.board.dto.response.CarFindResponseDto;
import com.alldriver.alldriver.board.dto.response.JobFindResponseDto;
import com.alldriver.alldriver.board.dto.response.MainLocationFindResponseDto;
import com.alldriver.alldriver.board.dto.response.SubLocationFindResponseDto;
import com.alldriver.alldriver.board.repository.CarRepository;
import com.alldriver.alldriver.board.repository.JobRepository;
import com.alldriver.alldriver.board.repository.MainLocationRepository;
import com.alldriver.alldriver.board.repository.SubLocationRepository;
import com.alldriver.alldriver.board.service.BoardCategoryRetrieveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BoardCategoryRetrieveServiceImpl implements BoardCategoryRetrieveService {
    private final JobRepository jobRepository;
    private final CarRepository carRepository;
    private final MainLocationRepository mainLocationRepository;
    private final SubLocationRepository subLocationRepository;
    @Override
    public List<JobFindResponseDto> findAllJobs() {
        return jobRepository.findAll()
                .stream()
                .map(job -> new JobFindResponseDto(job.getId(), job.getCategory()))
                .collect(Collectors.toList());
    }

    @Override
    public List<CarFindResponseDto> findAllCars() {
        return carRepository.findAll()
                .stream()
                .map(car -> new CarFindResponseDto(car.getId(), car.getCategory()))
                .collect(Collectors.toList());
    }

    @Override
    public List<MainLocationFindResponseDto> findAllMainLocations() {
        return mainLocationRepository.findAll()
                .stream()
                .map(mainLocation -> new MainLocationFindResponseDto(mainLocation.getId(), mainLocation.getCategory()))
                .collect(Collectors.toList());
    }

    @Override
    public List<SubLocationFindResponseDto> findAllSubLocations() {
        return subLocationRepository.findAll()
                .stream()
                .map(subLocation -> new SubLocationFindResponseDto(subLocation.getId(), subLocation.getCategory()))
                .collect(Collectors.toList());

    }

    @Override
    public List<SubLocationFindResponseDto> findSubLocationsByMainLocation(Long id) {
        return subLocationRepository.findByMainLocation(id)
                .stream()
                .map(subLocation -> new SubLocationFindResponseDto(subLocation.getId(), subLocation.getCategory()))
                .collect(Collectors.toList());
    }
}
