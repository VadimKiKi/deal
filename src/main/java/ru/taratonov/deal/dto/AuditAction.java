package ru.taratonov.deal.dto;

import lombok.Builder;
import lombok.Data;
import ru.taratonov.deal.enums.AuditActionServiceType;
import ru.taratonov.deal.enums.AuditActionType;

import java.util.UUID;

@Data
@Builder
public class AuditAction {
    private UUID uuid;
    private AuditActionType type;
    private AuditActionServiceType serviceType;
    private String message;
}
