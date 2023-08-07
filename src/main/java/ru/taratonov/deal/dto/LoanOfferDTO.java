package ru.taratonov.deal.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class LoanOfferDTO {
    @NotNull(message = "must not be empty")
    @Min(value = 1, message = "must be greater than 0")
    private Long applicationId;

    @NotNull(message = "must not be empty")
    @DecimalMin(value = "10000.0", message = "must be greater or equal than 10000")
    private BigDecimal requestedAmount;

    @NotNull(message = "must not be empty")
    @DecimalMin(value = "10000.0", message = "must be greater or equal than 10000")
    private BigDecimal totalAmount;

    @NotNull(message = "must not be empty")
    @Min(value = 6, message = "must be greater or equal than 6")
    private Integer term;

    @NotNull(message = "must not be empty")
    @DecimalMin(value = "0.1", message = "must be greater than 0")
    private BigDecimal monthlyPayment;

    @NotNull(message = "must not be empty")
    @DecimalMin(value = "0.1", message = "must be greater than 0")
    private BigDecimal rate;

    @NotNull(message = "must not be empty")
    private Boolean isInsuranceEnabled;

    @NotNull(message = "must not be empty")
    private Boolean isSalaryClient;
}
