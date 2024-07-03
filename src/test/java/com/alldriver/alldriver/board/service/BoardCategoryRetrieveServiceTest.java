package com.alldriver.alldriver.board.service;

import com.alldriver.alldriver.board.domain.Car;
import com.alldriver.alldriver.board.domain.Job;
import com.alldriver.alldriver.board.domain.MainLocation;
import com.alldriver.alldriver.board.domain.SubLocation;
import com.alldriver.alldriver.board.dto.response.CarFindResponseDto;
import com.alldriver.alldriver.board.dto.response.JobFindResponseDto;
import com.alldriver.alldriver.board.dto.response.MainLocationFindResponseDto;
import com.alldriver.alldriver.board.dto.response.SubLocationFindResponseDto;
import com.alldriver.alldriver.board.repository.CarRepository;
import com.alldriver.alldriver.board.repository.JobRepository;
import com.alldriver.alldriver.board.repository.MainLocationRepository;
import com.alldriver.alldriver.board.repository.SubLocationRepository;
import com.alldriver.alldriver.board.service.impl.BoardCategoryRetrieveServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardCategoryRetrieveServiceTest {
    @InjectMocks
    private BoardCategoryRetrieveServiceImpl boardCategoryRetrieveService;
    @Mock
    private JobRepository jobRepository;
    @Mock
    private CarRepository carRepository;
    @Mock
    private SubLocationRepository subLocationRepository;
    @Mock
    private MainLocationRepository mainLocationRepository;

    @Test
    @DisplayName("직종 정보 전체 조회")
    void findAllJobs() {
        // given
        List<Job> jobs = setUpJobs();
        when(jobRepository.findAll()).thenReturn(jobs);
        // when
        List<JobFindResponseDto> result = boardCategoryRetrieveService.findAllJobs();
        // then
        assertThat(result).hasSize(10);
    }

    @Test
    @DisplayName("차종 정보 전체 조회")
    void findAllCars() {
        // given
        List<Car> cars = setUpCars();
        when(carRepository.findAll()).thenReturn(cars);
        // when
        List<CarFindResponseDto> result = boardCategoryRetrieveService.findAllCars();
        // then
        assertThat(result).hasSize(10);
    }

    @Test
    @DisplayName("지역(시) 정보 전체 조회")
    void findAllMainLocations() {
        // given
        List<MainLocation> mainLocations = setUpMainLocations();
        when(mainLocationRepository.findAll()).thenReturn(mainLocations);
        // when
        List<MainLocationFindResponseDto> result = boardCategoryRetrieveService.findAllMainLocations();
        // then
        assertThat(result).hasSize(10);
    }

    @Test
    @DisplayName("지역(구) 정보 전체 조회")
    void findAllSubLocations() {
        // given
        MainLocation mainLocation = MainLocation.builder()
                .category("test")
                .build();
        List<SubLocation> subLocations = setUpSubLocations(mainLocation);
        when(subLocationRepository.findAll()).thenReturn(subLocations);

        // when
        List<SubLocationFindResponseDto> result = boardCategoryRetrieveService.findAllSubLocations();
        // then
        assertThat(result).hasSize(10);

    }

    @Test
    @DisplayName("지역(구) 정보를 지역(시) 정보에 따라 조회 성공")
    void findSubLocationsByMainLocation() {
        // given
        Long mainLocationId = 1L;
        MainLocation mainLocation = MainLocation.builder()
                .id(mainLocationId)
                .category("test")
                .build();
        List<SubLocation> subLocations = setUpSubLocations(mainLocation);
        when(subLocationRepository.findByMainLocation(mainLocationId)).thenReturn(subLocations);

        // when
        List<SubLocationFindResponseDto> result = boardCategoryRetrieveService.findSubLocationsByMainLocation(mainLocationId);

        // then
        assertThat(result).hasSize(10);
    }

    @Test
    @DisplayName("지역(구) 정보를 지역(시) 정보에 따라 조회 실패")
    void findSubLocationsByWrongMainLocation() {
        // given
        Long wrongMainLocationId = 0L;
        when(subLocationRepository.findByMainLocation(wrongMainLocationId)).thenReturn(new ArrayList<>());

        // when
        List<SubLocationFindResponseDto> result = boardCategoryRetrieveService.findSubLocationsByMainLocation(wrongMainLocationId);

        // then
        assertThat(result).isEmpty();
    }

    private List<Job> setUpJobs(){
        List<Job> jobs = new ArrayList<>();
        for (int i = 0; i<10; i++) {
            Job job = Job.builder()
                    .category("test" + 1)
                    .build();
            jobs.add(job);
        }
        return jobs;
    }
    private List<Car> setUpCars(){
        List<Car> cars = new ArrayList<>();
        for (int i = 0; i<10; i++) {
            Car car = Car.builder()
                    .category("test" + 1)
                    .build();
            cars.add(car);
        }
        return cars;
    }
    private List<SubLocation> setUpSubLocations(MainLocation mainLocation){
        List<SubLocation> subLocations = new ArrayList<>();
        for (int i = 0; i<10; i++) {
            SubLocation subLocation = SubLocation.builder()
                    .category("test" + 1)
                    .mainLocation(mainLocation)
                    .build();
            subLocations.add(subLocation);
        }
        return subLocations;
    }
    private List<MainLocation> setUpMainLocations(){
        List<MainLocation> mainLocations = new ArrayList<>();
        for (int i = 0; i<10; i++) {
            MainLocation mainLocation = MainLocation.builder()
                    .category("test" + 1)
                    .build();
            mainLocations.add(mainLocation);
        }
        return mainLocations;
    }
}