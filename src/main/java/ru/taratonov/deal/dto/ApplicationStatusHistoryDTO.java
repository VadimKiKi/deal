package ru.taratonov.deal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import ru.taratonov.deal.enums.ApplicationStatus;
import ru.taratonov.deal.enums.ChangeType;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@Accessors(chain = true)
public class ApplicationStatusHistoryDTO {
    private ApplicationStatus status;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate time;
    private ChangeType changeType;
}
