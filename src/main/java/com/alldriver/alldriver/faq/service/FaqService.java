package com.alldriver.alldriver.faq.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.alldriver.alldriver.faq.domain.Faq;
import com.alldriver.alldriver.faq.dto.request.FaqSaveRequestDto;
import com.alldriver.alldriver.faq.dto.request.FaqUpdateRequestDto;

import com.alldriver.alldriver.faq.dto.response.FaqFindResponseDto;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FaqService {
    private final DynamoDBMapper dynamoDBMapper;
    private final AmazonDynamoDB amazonDynamoDB;

    private void createFAQTableIfNotExists() {
        CreateTableRequest createTableRequest = dynamoDBMapper.generateCreateTableRequest(Faq.class)
                .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
        TableUtils.createTableIfNotExists(amazonDynamoDB, createTableRequest);
    }

    public List<FaqFindResponseDto> findAll(){
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        List<Faq> result = dynamoDBMapper.scan(Faq.class, scanExpression);

        List<FaqFindResponseDto> dtoList = result.stream()
                .map(FaqFindResponseDto::new)
                .collect(Collectors.toList());

        return dtoList;
    }


    public String saveFaq(FaqSaveRequestDto faqSaveRequestDto) {
        this.createFAQTableIfNotExists();

        Date createdAt = new Date();
        dynamoDBMapper.save(faqSaveRequestDto.toEntity(createdAt));

        return "자주 묻는 질문 저장 완료.";
    }


    public String updateFaq(FaqUpdateRequestDto faqUpdateRequestDto){
        String content = faqUpdateRequestDto.getContent();
        String title = faqUpdateRequestDto.getTitle();
        String id = faqUpdateRequestDto.getId();
        Date createdAt = faqUpdateRequestDto.getCreatedAt();

        Faq load = dynamoDBMapper.load(Faq.class, id, createdAt);

        load.setContent(content);
        load.setTitle(title);

        dynamoDBMapper.save(load);

        return "자주 묻는 질문 업데이트 완료.";
    }

    public String deleteFaq(String id, Date createdAt) {
        Faq load = dynamoDBMapper.load(Faq.class, id, createdAt);

        dynamoDBMapper.delete(load);

        return "자주 묻는 질문 삭제 완료.";
    }

}
