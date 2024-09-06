package com.alldriver.alldriver.user.controller;

import com.alldriver.alldriver.user.dto.request.FcmSendRequestDto;
import com.alldriver.alldriver.user.service.FcmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    @Operation(summary = "알림 전송")
    public ResponseEntity<String> sendMessage(@RequestBody
                                              @Valid FcmSendRequestDto fcmSendRequestDto){
        return ResponseEntity.ok(fcmService.sendMessage(fcmSendRequestDto));
    }
}
