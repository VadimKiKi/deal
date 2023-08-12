package ru.taratonov.deal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentScheduleElement {
    @Schema(
            description = "payment number",
            name = "number",
            example = "1")
    private Integer number;

    @Schema(
            description = "payment date",
            name = "date",
            example = "2023-12-11")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;

    @Schema(
            description = "the full amount of the monthly payment",
            name = "totalPayment",
            example = "11000")
    private BigDecimal totalPayment;

    @Schema(
            description = "the amount of the interest payment",
            name = "interestPayment",
            example = "1200")
    private BigDecimal interestPayment;

    @Schema(
            description = "the amount to repay the loan body",
            name = "debtPayment",
            example = "5785")
    private BigDecimal debtPayment;

    @Schema(
            description = "remaining debt",
            name = "remainingDebt",
            example = "235367")
    private BigDecimal remainingDebt;
}
