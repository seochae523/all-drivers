package com.alldriver.alldriver.board.controller;

import com.alldriver.alldriver.board.dto.response.CarFindResponseDto;
import com.alldriver.alldriver.board.dto.response.JobFindResponseDto;
import com.alldriver.alldriver.board.dto.response.MainLocationFindResponseDto;
import com.alldriver.alldriver.board.dto.response.SubLocationFindResponseDto;
import com.alldriver.alldriver.board.service.BoardCategoryRetrieveService;
import com.alldriver.alldriver.common.enums.ValidationError;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "게시글 카테고리 조회 관련 api")
public class BoardCategoryRetrieveController {

    private final BoardCategoryRetrieveService boardCategoryRetrieveService;

    @GetMapping("/jobs")
    public ResponseEntity<List<JobFindResponseDto>> findAllJobs(){
        return ResponseEntity.ok(boardCategoryRetrieveService.findAllJobs());
    }

    @GetMapping("/cars")
    public ResponseEntity<List<CarFindResponseDto>> findAllCars(){
        return ResponseEntity.ok(boardCategoryRetrieveService.findAllCars());
    }

    @GetMapping("/mainLocations")
    public ResponseEntity<List<MainLocationFindResponseDto>> findAllMainLocations(){
        return ResponseEntity.ok(boardCategoryRetrieveService.findAllMainLocations());
    }

    @GetMapping("/subLocations")
    public ResponseEntity<List<SubLocationFindResponseDto>> findAllSubLocations(){
        return ResponseEntity.ok(boardCategoryRetrieveService.findAllSubLocations());
    }

    @GetMapping("/subLocations/{mainLocationId}")
    @Parameter(name = "mainLocationId", description = "시 (main location) id")
    public ResponseEntity<List<SubLocationFindResponseDto>> findSubLocationsByMainLocationId(@PathVariable
                                                                                                 @NotNull(message = ValidationError.Message.MAIN_LOCATION_ID_NOT_FOUND) Long mainLocationId){
        return ResponseEntity.ok(boardCategoryRetrieveService.findSubLocationsByMainLocation(mainLocationId));
    }

}
