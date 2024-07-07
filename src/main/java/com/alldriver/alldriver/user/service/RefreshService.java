package com.alldriver.alldriver.user.service;

import com.alldriver.alldriver.common.token.dto.AuthToken;
import com.alldriver.alldriver.common.token.dto.RefreshRequestDto;
import org.json.simple.parser.ParseException;

public interface RefreshService {
    AuthToken refresh(RefreshRequestDto refreshRequestDto) throws ParseException;
}
