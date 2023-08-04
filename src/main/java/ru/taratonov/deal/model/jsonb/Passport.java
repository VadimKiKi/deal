package ru.taratonov.deal.model.jsonb;

import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Data
public class Passport {
    private String series;
    private String number;
    private String issueBranch;
    private LocalDate issueDate;
}
