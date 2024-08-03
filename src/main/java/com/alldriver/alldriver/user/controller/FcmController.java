package com.alldriver.alldriver.user.controller;

import com.alldriver.alldriver.user.dto.request.FcmSendRequestDto;
import com.alldriver.alldriver.user.service.FcmService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/fcm")
@Tag(name = "알림 관련 api")
@RequiredArgsConstructor
public class FcmController {
    private final FcmService fcmService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody FcmSendRequestDto fcmSendRequestDto){
        return ResponseEntity.ok(fcmService.sendMessage(fcmSendRequestDto));
    }
}
