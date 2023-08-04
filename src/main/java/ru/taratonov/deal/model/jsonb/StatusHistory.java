package ru.taratonov.deal.model.jsonb;

import lombok.Data;
import ru.taratonov.deal.enums.ChangeType;

import java.time.LocalDate;

@Data
public class StatusHistory {
    private String status;
    private LocalDate time;
    private ChangeType changeType;
}
