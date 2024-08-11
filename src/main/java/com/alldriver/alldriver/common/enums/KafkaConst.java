package com.alldriver.alldriver.common.util;

import org.springframework.beans.factory.annotation.Value;


public class KafkaConst {

    @Value("${spring.kafka.consumer.group-id}")
    public static String GROUP_ID;
    public static final String TOPIC = "chat-messages";
}
