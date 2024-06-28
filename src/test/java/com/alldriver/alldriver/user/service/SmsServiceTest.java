package com.alldriver.alldriver.user.service;

import com.alldriver.alldriver.user.repository.SmsRepository;
import com.alldriver.alldriver.user.repository.UserRepository;
import com.alldriver.alldriver.user.service.impl.SmsServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class SmsServiceTest {
    @InjectMocks
    private SmsServiceImpl smsService;

    @Mock
    private SmsRepository smsRepository;

    @Mock
    private UserRepository userRepository;


}