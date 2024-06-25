package com.alldriver.alldriver.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.net.URISyntaxException;

public interface OpenApiService {
    Boolean validateLicense(String license) throws JsonProcessingException, URISyntaxException;
    Boolean validateCar(String carNumber);
}
