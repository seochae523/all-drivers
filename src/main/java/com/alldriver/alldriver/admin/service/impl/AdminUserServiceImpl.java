package com.alldriver.alldriver.admin.service.impl;

import com.alldriver.alldriver.admin.dto.response.AdminCompanyInformationResponseDto;
import com.alldriver.alldriver.admin.dto.response.AdminUserCarInformationResponseDto;
import com.alldriver.alldriver.admin.dto.response.AdminUserFindResponseDto;
import com.alldriver.alldriver.admin.dto.response.AdminUserGrantResponseDto;
import com.alldriver.alldriver.admin.service.AdminUserService;
import com.alldriver.alldriver.common.enums.ErrorCode;
import com.alldriver.alldriver.common.exception.CustomException;
import com.alldriver.alldriver.user.domain.CompanyInformation;
import com.alldriver.alldriver.user.domain.User;
import com.alldriver.alldriver.user.domain.UserCarInformation;
import com.alldriver.alldriver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.alldriver.alldriver.common.configuration.MapperConfig.modelMapper;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminUserServiceImpl implements AdminUserService {
    private final UserRepository userRepository;

    @Value("${spring.data.rest.default-page-size}")
    private Integer pageSize;
    @Override
    public AdminUserGrantResponseDto grantUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        user.grantUser();
        User save = userRepository.save(user);

        return AdminUserGrantResponseDto.builder()
                .id(save.getId())
                .userId(save.getUserId())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminUserFindResponseDto> findAll(Integer page) {
        List<AdminUserFindResponseDto> adminUserFindResponseDtos = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<User> result = userRepository.findAll(pageable);

        for (User user : result) {
            Set<CompanyInformation> companyInformation = user.getCompanyInformation();
            List<AdminCompanyInformationResponseDto> companyInformationResponseDto = companyInformation.stream()
                    .map(info -> modelMapper.map(info, AdminCompanyInformationResponseDto.class))
                    .toList();

            Set<UserCarInformation> userCarInformation = user.getUserCarInformation();
            List<AdminUserCarInformationResponseDto> userCarInformationResponseDto = userCarInformation.stream()
                    .map(info -> modelMapper.map(info, AdminUserCarInformationResponseDto.class))
                    .toList();

            AdminUserFindResponseDto adminUserFindResponseDto = modelMapper.map(user, AdminUserFindResponseDto.class);
            adminUserFindResponseDto.setUserCarInformation(userCarInformationResponseDto);
            adminUserFindResponseDto.setCompanyInformation(companyInformationResponseDto);

            adminUserFindResponseDtos.add(adminUserFindResponseDto);

        }

        return adminUserFindResponseDtos;
    }
}
