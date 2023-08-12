package ru.taratonov.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.taratonov.deal.enums.Gender;
import ru.taratonov.deal.enums.MaritalStatus;

import java.time.LocalDate;

@Data
public class FinishRegistrationRequestDTO {
    @Schema(
            description = "person's gender",
            name = "gender",
            example = "MALE")
    private Gender gender;

    @Schema(
            description = "person's marital status",
            name = "maritalStatus",
            example = "MARRIED")
    private MaritalStatus maritalStatus;

    @Schema(
            description = "number of dependents",
            name = "dependentAmount",
            example = "1")
    private Integer dependentAmount;

    @Schema(
            description = "date of issue of the passport",
            name = "passportIssueDate",
            example = "2010-01-01")
    private LocalDate passportIssueDate;

    @Schema(
            description = "passport issuing department",
            name = "passportIssueBranch",
            example = "ГУ МВД РОССИИ")
    private String passportIssueBranch;

    @Schema(
            description = "information about person at work",
            name = "employment")
    private EmploymentDTO employment;

    @Schema(
            description = "person's account",
            name = "account",
            example = "124353")
    private String account;
}
