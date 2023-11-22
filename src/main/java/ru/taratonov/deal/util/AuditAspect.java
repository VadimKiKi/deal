package ru.taratonov.deal.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.taratonov.deal.enums.AuditActionType;
import ru.taratonov.deal.service.DocumentKafkaService;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private final DocumentKafkaService documentKafkaService;


    @Around("@annotation(ru.taratonov.deal.annotation.Audit)")
    public Object AuditActionToAuditMs(ProceedingJoinPoint joinPoint) {
        documentKafkaService.sendMessage(joinPoint,AuditActionType.START);

        Object proceed;
        try {
            proceed = joinPoint.proceed();
        } catch (Throwable e) {
            documentKafkaService.sendMessage(joinPoint,AuditActionType.FAILURE);
            throw new RuntimeException(e);
        }

        documentKafkaService.sendMessage(joinPoint, AuditActionType.SUCCESS);
        return proceed;
    }
}
