package ru.taratonov.deal.dto;

import lombok.Data;
import ru.taratonov.deal.enums.Gender;
import ru.taratonov.deal.enums.MaritalStatus;

import java.time.LocalDate;

@Data
public class FinishRegistrationRequestDTO {
    private Gender gender;
    private MaritalStatus maritalStatus;
    private Integer dependentAmount;
    private LocalDate passportIssueDate;
    private String passportIssueBranch;
    private EmploymentDTO employment;
    private String account;
}
