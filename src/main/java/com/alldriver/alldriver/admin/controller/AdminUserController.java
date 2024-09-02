package com.alldriver.alldriver.admin.controller;

import com.alldriver.alldriver.admin.dto.response.AdminUserFindResponseDto;
import com.alldriver.alldriver.admin.dto.response.AdminUserGrantResponseDto;
import com.alldriver.alldriver.admin.service.AdminUserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Tag(name ="관리자 api")
@Validated
public class AdminUserController {
    private final AdminUserService adminUserService;

    @PutMapping("/user/grant")
    @Parameter(name="id", description = "user의 id (로그인 할때 쓰는 id아님)")
    public ResponseEntity<AdminUserGrantResponseDto> grantUser(@RequestParam(name = "id") Long id){
        return ResponseEntity.ok(adminUserService.grantUser(id));
    }

    @GetMapping("/user/all")
    public ResponseEntity<List<AdminUserFindResponseDto>> findAll(@RequestParam(name = "page") Integer page){
        return ResponseEntity.ok(adminUserService.findAll(page));
    }
}
