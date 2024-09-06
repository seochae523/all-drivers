package com.alldriver.alldriver.user.service;

import com.alldriver.alldriver.user.dto.response.LicenseValidateDetailResponseDto;
import com.alldriver.alldriver.user.dto.response.LicenseValidateResponseDto;

import com.alldriver.alldriver.user.service.impl.UserValidationServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;



@RestClientTest(value = {UserValidationServiceImpl.class})
@ActiveProfiles("test")
public class OpenApiServiceTest {
    @Autowired
    private UserValidationServiceImpl userValidateService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RestTemplateBuilder restTemplateBuilder;
    @Autowired
    private MockRestServiceServer mockServer;

    @Value("${open-api.license.base-url}")
    private String licenseApiBaseUrl;

    @Value("${open-api.license.api-key}")
    private String licenseOpenApiKey;

    @BeforeEach
    public void setup() {
        RestTemplate restTemplate = restTemplateBuilder.build();
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }



    private String createLicenseValidateResponse(String type) throws JsonProcessingException {
        switch (type){
            case "성공":
                LicenseValidateDetailResponseDto rightDetail = LicenseValidateDetailResponseDto.builder()
                        .bNo("2208162517")
                        .bStt("계속사업자")
                        .bSttCd("01")
                        .taxType("부가가치세 일반과세자")
                        .taxTypeCd("01")
                        .endDt("")
                        .uTccYn("N")
                        .taxTypeChangeDt("20000101")
                        .invoiceApplyAt("20000101")
                        .rbfTaxType("부가가치세 일반과세자")
                        .rbfTaxTypeCd("01")
                        .build();

                LicenseValidateResponseDto rightResult = LicenseValidateResponseDto.builder()
                        .statusCode("OK")
                        .matchCnt(1)
                        .requestCnt(1)
                        .data(rightDetail)
                        .build();
                return objectMapper.writeValueAsString(rightResult);


            case "실패":
                LicenseValidateDetailResponseDto wrongDetail = LicenseValidateDetailResponseDto.builder()
                        .bNo("220816251")
                        .bStt("계속사업자")
                        .bSttCd("01")
                        .taxType("부가가치세 일반과세자")
                        .taxTypeCd("01")
                        .endDt("")
                        .uTccYn("N")
                        .taxTypeChangeDt("20000101")
                        .invoiceApplyAt("20000101")
                        .rbfTaxType("부가가치세 일반과세자")
                        .rbfTaxTypeCd("01")
                        .build();

                LicenseValidateResponseDto wrongResult = LicenseValidateResponseDto.builder()
                        .statusCode("OK")
                        .requestCnt(1)
                        .data(wrongDetail)
                        .build();
                return objectMapper.writeValueAsString(wrongResult);

        }
        return null;
    }
}
