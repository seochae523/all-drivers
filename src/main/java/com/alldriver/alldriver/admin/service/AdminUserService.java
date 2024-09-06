package com.alldriver.alldriver.admin.service;

import com.alldriver.alldriver.admin.dto.response.AdminUserFindResponseDto;
import com.alldriver.alldriver.admin.dto.response.AdminUserGrantResponseDto;

import java.util.List;

public interface AdminUserService {
    AdminUserGrantResponseDto grantUser(Long id);

    List<AdminUserFindResponseDto> findAll(Integer page);
}
