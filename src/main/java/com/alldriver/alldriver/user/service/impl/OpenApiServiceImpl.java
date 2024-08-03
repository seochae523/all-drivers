package com.alldriver.alldriver.user.service.impl;

import com.alldriver.alldriver.user.service.OpenApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class OpenApiServiceImpl implements OpenApiService {
    @Value("${open-api.license.api-key}")
    private String licenseApiKey;
    @Value("${open-api.license.base-url}")
    private String licenseValidateOpenApiBaseUrl;


    @Override
    public Boolean validateLicense(String license) throws URISyntaxException {
        final String requestUrl = licenseValidateOpenApiBaseUrl + "/status?serviceKey=" + licenseApiKey ;
        RestTemplate restTemplate = new RestTemplate();

        URI uri = new URI(requestUrl);

        List<String> licenseNo = new ArrayList<>();
        licenseNo.add(license);
        Map<String, List<String>> bodyMap = new HashMap<>();

        bodyMap.put("b_no", licenseNo);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, List<String>>> requestEntity = new HttpEntity<>(bodyMap, headers);

        Map<String, Object> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, Map.class).getBody();

        if(Optional.ofNullable(response.get("match_cnt")).isPresent()){
            return true;
        }
        return false;
    }

    @Override
    public Boolean validateCar(String carNumber) {
        return null;
    }

}
