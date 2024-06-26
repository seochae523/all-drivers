package com.alldriver.alldriver.user.service;

import com.alldriver.alldriver.user.dto.response.LicenseValidateDetailResponseDto;
import com.alldriver.alldriver.user.dto.response.LicenseValidateResponseDto;
import com.alldriver.alldriver.user.service.impl.OpenApiServiceImpl;
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
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;



@RestClientTest(value = {OpenApiServiceImpl.class})
public class OpenApiServiceTest {
    @Autowired
    private OpenApiServiceImpl openApiService;
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

    @Test
    @DisplayName("사업자 등록 번호 조회 성공")
    void 사업자_등록_번호_조회_성공() throws JsonProcessingException, URISyntaxException {
        // given
        String license = "2208162517";
        String expectedJsonResponse = createLicenseValidateResponse("성공");
        String requestUrl = licenseApiBaseUrl+"/status?serviceKey=" + licenseOpenApiKey;
        URI uri = new URI(requestUrl);

        mockServer.expect(requestTo(uri))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(objectMapper.writeValueAsString(expectedJsonResponse), MediaType.APPLICATION_JSON));
        // when
        Boolean result = openApiService.validateLicense(license);
        // then
        assertThat(result).isEqualTo(true);
    }

    @Test
    @DisplayName("사업자 등록 번호 조회 실패")
    void 사업자_등록_번호_조회_실패() throws JsonProcessingException, URISyntaxException {
        // given
        String license = "220816251";
        String expectedJsonResponse = createLicenseValidateResponse("실패");
        String requestUrl = licenseApiBaseUrl+"/status?serviceKey=" + licenseOpenApiKey;
        URI uri = new URI(requestUrl);

        mockServer.expect(requestTo(uri))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(objectMapper.writeValueAsString(expectedJsonResponse), MediaType.APPLICATION_JSON));
        // when
        Boolean result = openApiService.validateLicense(license);
        // then
        assertThat(result).isEqualTo(false);
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
