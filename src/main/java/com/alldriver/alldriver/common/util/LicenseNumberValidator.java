package com.alldriver.alldriver.common.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class LicenseNumberValidator {

    @Value("${open-api.license.api-key}")
    private String licenseApiKey;
    @Value("${open-api.license.base-url}")
    private String licenseValidateOpenApiBaseUrl;
    public Boolean validateLicense(String license) throws URISyntaxException {
        log.info("{}", licenseApiKey);
        final String requestUrl = licenseValidateOpenApiBaseUrl + "/status?serviceKey=" + licenseApiKey;
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

        List<Map<String, String>> data = (List<Map<String, String>>) response.get("data");
        if(Optional.ofNullable(response.get("match_cnt")).isPresent() && data.get(0).get("end_dt").isEmpty()){
            return true;
        }
        return false;
    }
}
