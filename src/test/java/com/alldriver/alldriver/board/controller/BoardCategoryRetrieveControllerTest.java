package com.alldriver.alldriver.board.controller;


import com.alldriver.alldriver.board.dto.response.*;
import com.alldriver.alldriver.board.service.BoardCategoryRetrieveService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;
import java.util.List;


import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BoardCategoryRetrieveController.class)
@WithMockUser("test")
class BoardCategoryRetrieveControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    BoardCategoryRetrieveService boardCategoryRetrieveService;

    @Test
    void findAllJobs() throws Exception {
        // given
        List<JobFindResponseDto> jobs = setUpJobs();
        when(boardCategoryRetrieveService.findAllJobs()).thenReturn(jobs);

        // when, then
        mockMvc.perform(get("/user/jobs"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(jobs)));
    }

    @Test
    void findAllCars() throws Exception {
        // given
        List<CarFindResponseDto> cars = setUpCars();
        when(boardCategoryRetrieveService.findAllCars()).thenReturn(cars);

        // when, then
        mockMvc.perform(get("/user/cars"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(cars)));

    }

    @Test
    void findAllMainLocations() throws Exception {
        // given
        List<MainLocationFindResponseDto> mainLocations = setUpMainLocations();
        when(boardCategoryRetrieveService.findAllMainLocations()).thenReturn(mainLocations);

        // when, then
        mockMvc.perform(get("/user/mainLocations"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mainLocations)));

    }

    @Test
    void findAllSubLocations() throws Exception {
        // given
        List<SubLocationFindResponseDto> subLocations = setUpSubLocations();
        when(boardCategoryRetrieveService.findAllSubLocations()).thenReturn(subLocations);

        // when, then
        mockMvc.perform(get("/user/subLocations"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(subLocations)));
    }

    @Test
    void findSubLocationsByMainLocationId() throws Exception {
        // given
        Long mainLocationId = 1L;
        List<SubLocationFindResponseDto> subLocations = setUpSubLocations();
        when(boardCategoryRetrieveService.findSubLocationsByMainLocation(mainLocationId)).thenReturn(subLocations);

        // when, then
        mockMvc.perform(get("/user/subLocations/" + mainLocationId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(subLocations)));
    }

    private List<JobFindResponseDto> setUpJobs(){
        List<JobFindResponseDto> result = new ArrayList<>();
        for (int i =0 ;i<10; i++) {
            result.add(new JobFindResponseDto((long)i, "test job"));
        }
        return result;
    }
    private List<CarFindResponseDto> setUpCars(){
        List<CarFindResponseDto> result = new ArrayList<>();
        for (int i =0 ;i<10; i++) {
            result.add(new CarFindResponseDto((long)i, "test car"));
        }
        return result;
    }
    private List<SubLocationFindResponseDto> setUpSubLocations(){
        List<SubLocationFindResponseDto> result = new ArrayList<>();
        for (int i =0 ;i<10; i++) {
            result.add(new SubLocationFindResponseDto((long)i, "test Sub Location"));
        }
        return result;
    }

    private List<MainLocationFindResponseDto> setUpMainLocations(){
        List<MainLocationFindResponseDto> result = new ArrayList<>();
        for (int i =0 ;i<10; i++) {
            result.add(new MainLocationFindResponseDto((long)i, "test Main Location"));
        }
        return result;
    }

}