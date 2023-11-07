package ru.taratonov.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.taratonov.deal.enums.Theme;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class EmailMessageDTO {
    @Schema(
            description = "client's email",
            name = "emailAddress",
            example = "taratonovv8@bk.ru")
    private String emailAddress;

    @Schema(
            description = "Theme of email message",
            name = "theme",
            example = "CREATE_DOCUMENTS")
    private Theme theme;

    @Schema(
            description = "application id",
            name = "applicationId",
            example = "1")
    private Long applicationId;
}
