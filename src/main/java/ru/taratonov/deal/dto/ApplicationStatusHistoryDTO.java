package ru.taratonov.deal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.taratonov.deal.enums.ApplicationStatus;
import ru.taratonov.deal.enums.ChangeType;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
public class ApplicationStatusHistoryDTO {
    @Schema(
            description = "application status",
            name = "status",
            example = "PREAPPROVAL")
    private ApplicationStatus status;

    @Schema(
            description = "time when status was changed",
            name = "time",
            example = "2023-10-12")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate time;

    @Schema(
            description = "type of status change",
            name = "changeType",
            example = "AUTOMATIC")
    private ChangeType changeType;
}
