package com.alldriver.alldriver.user.service;

import com.alldriver.alldriver.user.dto.response.AuthToken;
import com.alldriver.alldriver.user.dto.request.RefreshRequestDto;
import org.json.simple.parser.ParseException;

public interface RefreshService {
    AuthToken refresh(RefreshRequestDto refreshRequestDto) throws ParseException;
}
