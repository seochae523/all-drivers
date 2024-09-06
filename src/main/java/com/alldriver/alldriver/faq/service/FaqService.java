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

    public List<FaqFindResponseDto> findAll(){
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        List<Faq> result = dynamoDBMapper.scan(Faq.class, scanExpression);

        List<FaqFindResponseDto> dtoList = result.stream()
                .map(FaqFindResponseDto::new)
                .collect(Collectors.toList());

        return dtoList;
    }

}
