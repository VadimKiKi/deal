package ru.taratonov.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Accessors(chain = true)
public class ApplicationDTO {
    @Schema(
            description = "application id",
            name = "applicationId",
            example = "1")
    private Long applicationId;

    @Schema(
            description = "first name of person",
            name = "firstName",
            example = "Vadim")
    private String firstName;

    @Schema(
            description = "last name of person",
            name = "lastName",
            example = "Taratonov")
    private String lastName;

    @Schema(
            description = "middle name of person",
            name = "middleName",
            example = "Nikolaevich")
    private String middleName;

    @Schema(
            description = "amount of the loan",
            name = "amount",
            example = "10000")
    private BigDecimal amount;

    @Schema(
            description = "loan term",
            name = "term",
            example = "7")
    private Integer term;

    @Schema(
            description = "monthly payment of the loan",
            name = "monthlyPayment",
            example = "1245.67")
    private BigDecimal monthlyPayment;

    @Schema(
            description = "loan rate",
            name = "rate",
            example = "3")
    private BigDecimal rate;

    @Schema(
            description = "full cost of the loan",
            name = "psk",
            example = "2.56")
    private BigDecimal psk;

    @Schema(
            description = "list of payments",
            name = "paymentSchedule")
    private List<PaymentScheduleElement> paymentSchedule;

    @Schema(
            description = "availability of credit insurance",
            name = "insuranceEnable",
            example = "false")
    private Boolean insuranceEnable;

    @Schema(
            description = "salary client",
            name = "salaryClient",
            example = "true")
    private Boolean salaryClient;

    @Schema(
            description = "day of creation loan request",
            name = "creationDate",
            example = "2023-02-04")
    private LocalDate creationDate;

    @Schema(
            description = "day of sign loan request",
            name = "signDate",
            example = "2023-04-07")
    private LocalDate signDate;

    @Schema(
            description = "ses code for client to sign documents",
            name = "sesCode",
            example = "2134")
    private Integer sesCode;
}
