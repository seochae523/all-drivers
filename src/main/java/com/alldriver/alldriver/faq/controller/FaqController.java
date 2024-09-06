package com.alldriver.alldriver.faq.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.alldriver.alldriver.faq.dto.request.FaqSaveRequestDto;
import com.alldriver.alldriver.faq.dto.request.FaqUpdateRequestDto;
import com.alldriver.alldriver.faq.dto.response.FaqFindResponseDto;
import com.alldriver.alldriver.faq.service.FaqService;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Validated
@Tag(name ="자주 묻는 질문 관련 api")
public class FaqController {
    private final FaqService faqService;

    @GetMapping("/faq/all")
    @Operation(summary = "자주 묻는 질문 조회")
    public ResponseEntity<List<FaqFindResponseDto>> findAll(){
        return ResponseEntity.ok(faqService.findAll());
    }
}

