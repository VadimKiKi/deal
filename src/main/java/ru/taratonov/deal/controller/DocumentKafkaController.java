package ru.taratonov.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.taratonov.deal.dto.ErrorDTO;
import ru.taratonov.deal.service.DocumentKafkaService;

@RestController
@RequestMapping("/deal/document")
@RequiredArgsConstructor
@Tag(name = "Document Controller", description = "Provides the ability to manage documents")
public class DocumentKafkaController {

    private final DocumentKafkaService documentKafkaService;

    @PostMapping("/{applicationId}/send")
    @Operation(summary = "Send documents", description = "Allows to send documents to client email")
    @ApiResponse(
            responseCode = "200",
            description = "Document has been sent!",
            content = @Content(mediaType = "application/json"))
    @ApiResponse(
            responseCode = "404",
            description = "Application not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDTO.class)))
    public void sendDocument(@Parameter(description = "Id of the application", required = true)
                             @PathVariable("applicationId") Long id) {
        documentKafkaService.sendDocuments(id);
    }

    @PostMapping("/{applicationId}/sign")
    @Operation(summary = "Request to sign documents", description = "Allows to send special code for singing documents")
    @ApiResponse(
            responseCode = "200",
            description = "Code has been sent!",
            content = @Content(mediaType = "application/json"))
    @ApiResponse(
            responseCode = "404",
            description = "Application not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDTO.class)))
    public void requestSignDocument(@Parameter(description = "Id of the application", required = true)
                                    @PathVariable("applicationId") Long id) {
        documentKafkaService.requestSignDocument(id);
    }

    @PostMapping("/{applicationId}/code")
    @Operation(summary = "Sign document with code", description = "Allows check special code and send email with successful loan request")
    @ApiResponse(
            responseCode = "200",
            description = "Credit issued!",
            content = @Content(mediaType = "application/json"))
    @ApiResponse(
            responseCode = "404",
            description = "Application not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDTO.class)))
    public void signDocumentWithCode(@Parameter(description = "Id of the application", required = true)
                                     @PathVariable("applicationId") Long id,
                                     @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                             description = "SES code")
                                     @RequestBody Integer sesCode) {
        documentKafkaService.signDocument(id, sesCode);
    }

    @PostMapping("/{applicationId}/deny")
    @Operation(summary = "Deny application", description = "Allows to deny the application")
    @ApiResponse(
            responseCode = "200",
            description = "Application denied!",
            content = @Content(mediaType = "application/json"))
    @ApiResponse(
            responseCode = "404",
            description = "Application not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDTO.class)))
    public void denyApplication(@Parameter(description = "Id of the application", required = true)
                                @PathVariable("applicationId") Long id) {
        documentKafkaService.denyApplication(id);
    }
}
