package com.alldriver.alldriver.user.service;

import com.alldriver.alldriver.user.dto.request.FcmSendRequestDto;

public interface FcmService {

    String sendMessage(FcmSendRequestDto fcmSendRequestDto);

}
