package com.alldriver.alldriver.admin.service.impl;

import com.alldriver.alldriver.admin.service.AdminFaqService;
import com.alldriver.alldriver.faq.domain.Faq;
import com.alldriver.alldriver.faq.dto.request.FaqSaveRequestDto;
import com.alldriver.alldriver.faq.dto.request.FaqUpdateRequestDto;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminFaqServiceImpl implements AdminFaqService {
    private final DynamoDBMapper dynamoDBMapper;
    private final AmazonDynamoDB amazonDynamoDB;

    private void createFAQTableIfNotExists() {
        CreateTableRequest createTableRequest = dynamoDBMapper.generateCreateTableRequest(Faq.class)
                .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
        TableUtils.createTableIfNotExists(amazonDynamoDB, createTableRequest);
    }

    @Override
    public String saveFaq(FaqSaveRequestDto faqSaveRequestDto) {
        this.createFAQTableIfNotExists();

        Date createdAt = new Date();
        dynamoDBMapper.save(faqSaveRequestDto.toEntity(createdAt));

        return "자주 묻는 질문 저장 완료.";
    }

    @Override
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

    @Override
    public String deleteFaq(String id, Date createdAt) {
        Faq load = dynamoDBMapper.load(Faq.class, id, createdAt);

        dynamoDBMapper.delete(load);

        return "자주 묻는 질문 삭제 완료.";
    }
}
