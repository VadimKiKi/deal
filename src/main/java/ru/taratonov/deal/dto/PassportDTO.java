package ru.taratonov.deal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
public class PassportDTO {
    @Schema(
            description = "passport series of person",
            name = "series",
            example = "2021")
    private String series;

    @Schema(
            description = "passport number of person",
            name = "number",
            example = "111111")
    private String number;

    @Schema(
            description = "passport issuing department",
            name = "issueBranch",
            example = "ГУ МВД РОССИИ")
    private String issueBranch;

    @Schema(
            description = "date of issue of the passport",
            name = "issueDate",
            example = "2010-01-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate issueDate;
}
