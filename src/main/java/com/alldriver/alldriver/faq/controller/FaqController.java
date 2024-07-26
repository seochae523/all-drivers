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
@RequestMapping
@Validated
@Tag(name ="자주 묻는 질문 관련 api")
public class FaqController {
    private final FaqService faqService;

    @GetMapping("/user/faq/all")
    @Operation(description = "자주 묻는 질문 조회")
    public ResponseEntity<List<FaqFindResponseDto>> findAll(){
        return ResponseEntity.ok(faqService.findAll());
    }

    @PostMapping("/admin/faq/save")
    @Operation(description = "자주 묻는 질문 저장")
    public ResponseEntity<String> save(@RequestBody @Valid FaqSaveRequestDto faqSaveRequestDto){
        return ResponseEntity.ok(faqService.saveFaq(faqSaveRequestDto));
    }
    @PutMapping("/admin/faq/update")
    @Operation(description = "자주 묻는 질문 업데이트")
    public ResponseEntity<String> update(@RequestBody @Valid FaqUpdateRequestDto faqUpdateRequestDto){
        return ResponseEntity.ok(faqService.updateFaq(faqUpdateRequestDto));
    }

    @DeleteMapping("/admin/faq/delete")
    @Operation(description = "자주 묻는 질문 삭제")
    public ResponseEntity<String> delete(@NotNull(message = "Faq Id Not Found.")
                                                           @RequestParam(value = "id", required = false) String id,
                                                       @NotNull(message = "Faq Created At Not Found.")
                                                       @RequestParam(value = "createdAt", required = false)
                                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date createdAt) throws ParseException {
        return ResponseEntity.ok(faqService.deleteFaq(id, createdAt));
    }

}

