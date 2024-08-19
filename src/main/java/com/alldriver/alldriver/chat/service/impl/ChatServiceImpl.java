//package com.alldriver.alldriver.chat.service.impl;
//
//import com.alldriver.alldriver.common.enums.KafkaConst;
//import com.alldriver.alldriver.common.util.JwtUtils;
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
//import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
//import com.amazonaws.services.dynamodbv2.model.AttributeValue;
//import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
//import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
//import com.amazonaws.services.dynamodbv2.util.TableUtils;
//import com.google.common.collect.ImmutableMap;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//import com.alldriver.alldriver.chat.domain.Chat;
//import com.alldriver.alldriver.chat.dto.KafkaChatDto;
//import com.alldriver.alldriver.chat.dto.request.ChatSaveRequestDto;
//import com.alldriver.alldriver.chat.dto.response.ChatFindResponseDto;
//import com.alldriver.alldriver.chat.dto.response.ChatSaveResponseDto;
//import com.alldriver.alldriver.chat.service.ChatService;
//
//
//import java.util.Date;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class ChatServiceImpl implements ChatService{
//    private final DynamoDBMapper dynamoDBMapper;
//    private final AmazonDynamoDB amazonDynamoDB;
//    private final KafkaTemplate<String, KafkaChatDto> kafkaTemplate;
//    private final SimpMessagingTemplate template;
//
//    private void createChatTableIfNotExists() {
//        CreateTableRequest createTableRequest = dynamoDBMapper.generateCreateTableRequest(Chat.class)
//                .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
//        TableUtils.createTableIfNotExists(amazonDynamoDB, createTableRequest);
//    }
//    @Override
//    public ChatSaveResponseDto chat(ChatSaveRequestDto chatSaveRequestDto) {
//        String sender = chatSaveRequestDto.getSender();
//        String message = chatSaveRequestDto.getMessage();
//        Long roomId = chatSaveRequestDto.getRoomId();
//
//        Date createdAt = new Date();
//
//        KafkaChatDto kafkaChatDto = new KafkaChatDto(chatSaveRequestDto, createdAt, sender);
//
//        this.send(KafkaConst.Value.TOPIC, kafkaChatDto);
//
//        this.saveChat(roomId, message, sender, createdAt);
//
//        return ChatSaveResponseDto.builder()
//                .roomId(roomId)
//                .createdAt(createdAt)
//                .message(message)
//                .sender(sender)
//                .build();
//    }
//
//    @Override
//    public List<ChatFindResponseDto> findAll(Long roomId) {
//        DynamoDBQueryExpression<Chat> objectDynamoDBQueryExpression = new DynamoDBQueryExpression<Chat>()
//                .withKeyConditionExpression("id = :id")
//                .withExpressionAttributeValues(ImmutableMap.of(":id", new AttributeValue().withN(roomId.toString())));
//
//        PaginatedQueryList<Chat> query = dynamoDBMapper.query(Chat.class, objectDynamoDBQueryExpression);
//
//        return query.stream()
//                .map(ChatFindResponseDto::new)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public ChatFindResponseDto findLastChat(Long roomId) {
//        DynamoDBQueryExpression<Chat> objectDynamoDBQueryExpression = new DynamoDBQueryExpression<Chat>()
//                .withKeyConditionExpression("id = :id")
//                .withExpressionAttributeValues(ImmutableMap.of(":id", new AttributeValue().withN(roomId.toString())))
//                .withLimit(1)
//                .withScanIndexForward(false);
//
//
//        PaginatedQueryList<Chat> result = dynamoDBMapper.query(Chat.class, objectDynamoDBQueryExpression);
//
//        if(result.isEmpty()) return new ChatFindResponseDto();
//
//        return new ChatFindResponseDto(result.get(0));
//
//    }
//
//
//    public void send(String topic, KafkaChatDto kafkaChatDto) {
//        kafkaTemplate.send(topic, kafkaChatDto);
//    }
//
//    @KafkaListener(topics = KafkaConst.Value.TOPIC, groupId = KafkaConst.Value.GROUP_ID, containerFactory = "kafkaChatContainerFactory")
//    public void consume(KafkaChatDto kafkaChatDto) {
//        template.convertAndSend("/sub/" + kafkaChatDto.getRoomId(), kafkaChatDto);
//    }
//
//    @Async
//    public void saveChat(Long roomId, String message, String sender, Date createdAt){
//        this.createChatTableIfNotExists();
//
//
//        Chat chat = Chat.builder()
//                .id(roomId)
//                .message(message)
//                .sender(sender)
//                .createdAt(createdAt)
//                .build();
//
//        dynamoDBMapper.save(chat);
//    }
//}
