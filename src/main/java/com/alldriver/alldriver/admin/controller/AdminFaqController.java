package com.alldriver.alldriver.admin.controller;

import com.alldriver.alldriver.admin.service.AdminFaqService;

import com.alldriver.alldriver.faq.dto.request.FaqSaveRequestDto;
import com.alldriver.alldriver.faq.dto.request.FaqUpdateRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Validated
@Tag(name ="관리자 api")
public class AdminFaqController {
    private final AdminFaqService adminFaqService;

    @PostMapping("/faq/save")
    @Operation(summary = "자주 묻는 질문 저장")
    public ResponseEntity<String> save(@RequestBody @Valid FaqSaveRequestDto faqSaveRequestDto){
        return ResponseEntity.ok(adminFaqService.saveFaq(faqSaveRequestDto));
    }
    @PutMapping("/faq/update")
    @Operation(summary = "자주 묻는 질문 업데이트")
    public ResponseEntity<String> update(@RequestBody @Valid FaqUpdateRequestDto faqUpdateRequestDto){
        return ResponseEntity.ok(adminFaqService.updateFaq(faqUpdateRequestDto));
    }

    @DeleteMapping("/faq/delete")
    @Operation(summary = "자주 묻는 질문 삭제", description = "createdAt은 yyyy-MM-dd'T'HH:mm:ss.SSSXXX 형태로 삽입")
    public ResponseEntity<String> delete(@NotNull(message = "Faq Id Not Found.") @RequestParam(value = "id", required = false) String id,
                                         @NotNull(message = "Faq Created At Not Found.")
                                         @RequestParam(value = "createdAt", required = false)
                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date createdAt) throws ParseException {
        return ResponseEntity.ok(adminFaqService.deleteFaq(id, createdAt));
    }
}
