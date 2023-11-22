package ru.taratonov.deal.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.taratonov.deal.dto.AuditAction;
import ru.taratonov.deal.enums.AuditActionServiceType;
import ru.taratonov.deal.enums.AuditActionType;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String KAFKA_TOPIC = "audit";

    @Around("@annotation(ru.taratonov.deal.annotation.ToAudit)")
    public Object AuditActionToAuditMs(ProceedingJoinPoint joinPoint) {
        sendMessage(joinPoint,AuditActionType.START);

        Object proceed;
        try {
            proceed = joinPoint.proceed();
        } catch (Throwable e) {
            sendMessage(joinPoint,AuditActionType.FAILURE);
            throw new RuntimeException(e);
        }

        sendMessage(joinPoint, AuditActionType.SUCCESS);
        return proceed;
    }

    private void sendMessage(ProceedingJoinPoint joinPoint, AuditActionType type) {
        AuditAction auditAction = AuditAction.builder()
                .type(type)
                .serviceType(AuditActionServiceType.DEAL)
                .message(createMessage(joinPoint))
                .build();
        try {
            sendMessageToKafka(auditAction);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public void sendMessageToKafka(AuditAction auditAction) throws JsonProcessingException {
        String resultToKafka = objectMapper.writeValueAsString(auditAction);
        kafkaTemplate.send(KAFKA_TOPIC, resultToKafka);
        log.info("message send to kafka with data {} to topic {}", resultToKafka, KAFKA_TOPIC);
    }

    private String createMessage(ProceedingJoinPoint joinPoint) {
        return String.join(" ",
                "Time",
                LocalDateTime.now().toString(),
                "Method",
                joinPoint.getSignature().getName(),
                "with parameters",
                Arrays.stream(joinPoint.getArgs())
                        .map(Object::toString)
                        .collect(Collectors.joining(", ")));
    }
}
