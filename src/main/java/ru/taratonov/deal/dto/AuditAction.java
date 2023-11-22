package ru.taratonov.deal.dto;

import lombok.Builder;
import lombok.Data;
import ru.taratonov.deal.enums.AuditActionServiceType;
import ru.taratonov.deal.enums.AuditActionType;

@Data
@Builder
public class AuditAction {
    private AuditActionType type;
    private AuditActionServiceType serviceType;
    private String message;
}
