package com.alldriver.alldriver.board.controller;

import com.alldriver.alldriver.board.dto.response.BoardCarFindResponseDto;
import com.alldriver.alldriver.board.dto.response.BoardJobFindResponseDto;
import com.alldriver.alldriver.board.dto.response.BoardLocationFindResponseDto;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardRetrieveControllerTest {

    @Test
    void findAllBoards() {
    }

    @Test
    void findByCars() {
    }

    @Test
    void findByJobs() {
    }

    @Test
    void findBySubLocations() {
    }

    @Test
    void findByMainLocation() {
    }

    private List<BoardJobFindResponseDto> setUpJobs(){
        List<BoardJobFindResponseDto> result = new ArrayList<>();
        for (int i =0 ;i<10; i++) {
            result.add(new BoardJobFindResponseDto((long)i, "test"));
        }
        return result;
    }
    private List<BoardCarFindResponseDto> setUpCars(){
        List<BoardCarFindResponseDto> result = new ArrayList<>();
        for (int i =0 ;i<10; i++) {
            result.add(new BoardCarFindResponseDto((long)i, "test"));
        }
        return result;
    }
    private List<BoardLocationFindResponseDto> setUpLocations(){
        List<BoardLocationFindResponseDto> result = new ArrayList<>();
        for (int i =0 ;i<10; i++) {
            result.add(new BoardLocationFindResponseDto((long)i, "test"));
        }
        return result;
    }
}