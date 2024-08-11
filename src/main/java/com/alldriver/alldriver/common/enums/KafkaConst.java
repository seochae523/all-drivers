package com.alldriver.alldriver.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum KafkaConst {
    GROUP_ID,
    TOPIC,
    BOOTSTRAP_SERVER;

    public static class Value{
        public static final String GROUP_ID = "chat-group";
        public static final String TOPIC = "chat-messages";
        public static final String BOOTSTRAP_SERVER = "kafka:9092";
    }
}
