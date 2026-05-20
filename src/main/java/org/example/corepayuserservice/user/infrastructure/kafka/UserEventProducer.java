package org.example.corepayuserservice.user.infrastructure.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.corepayuserservice.user.infrastructure.kafka.event.UserCreatedEvent;
import org.example.corepayuserservice.user.infrastructure.kafka.event.UserUpdatePasswordEvent;
import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventProducer {

    private final KafkaTemplate<String,String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Async
    public void sendUserCreated(UserCreatedEvent event){
        sendMessage("user-created-topic",event);
    }

    @Async
    public void sendUserUpdatePassword(UserUpdatePasswordEvent event){
        sendMessage("user-update-password-topic",event);
    }

    private void sendMessage(String topic, Object event) {
        try {
            String messagePayload = objectMapper.writeValueAsString(event);

            // 현재 스레드의 MDC에서 Trace ID 꺼내기
            String traceId = MDC.get("traceId");

            // MessageBuilder를 사용하여 페이로드(JSON)와 카프카 헤더(Trace ID)를 함께 포장
            Message<String> kafkaMessage = MessageBuilder
                    .withPayload(messagePayload)
                    .setHeader(KafkaHeaders.TOPIC, topic)
                    .setHeader("X-Trace-Id", traceId != null ? traceId : "UNKNOWN-TRACE")
                    .build();

            // 4. 포장된 메시지 전송
            kafkaTemplate.send(kafkaMessage);
            log.info("[카프카 발송 성공] 토픽: {}, TraceID: {}, 메시지: {}", topic, traceId, messagePayload);

        } catch (JsonProcessingException e) {
            log.error("카프카 메시지 직렬화 에러. 토픽: {}", topic, e);
        }
    }
}
