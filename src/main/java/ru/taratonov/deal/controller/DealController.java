package ru.taratonov.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.taratonov.deal.dto.ErrorDTO;
import ru.taratonov.deal.dto.FinishRegistrationRequestDTO;
import ru.taratonov.deal.dto.LoanApplicationRequestDTO;
import ru.taratonov.deal.dto.LoanOfferDTO;
import ru.taratonov.deal.service.DealService;

import java.util.List;

@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
@Tag(name = "Deal Controller", description = "Managing loan offers with using db")
public class DealController {

    private final DealService dealService;

    @PostMapping("/application")
    @Operation(summary = "Get loan offers", description = "Allows to get 4 loan offers for person")
    @ApiResponse(
            responseCode = "200",
            description = "List of offers received!",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LoanOfferDTO.class)))
    @ApiResponse(
            responseCode = "400",
            description = "Prescoring failed",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDTO.class)))
    public List<LoanOfferDTO> getPossibleLoanOffers(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Loan request",
            content = @Content(schema = @Schema(implementation = LoanApplicationRequestDTO.class)))
                                                    @RequestBody
                                                    @Valid LoanApplicationRequestDTO loanApplicationRequestDTO) {
        return dealService.getOffers(loanApplicationRequestDTO);
    }

    @PutMapping("/offer")
    @Operation(summary = "Choose one offer", description = "Allows to choose one of four offers")
    @ApiResponse(
            responseCode = "200",
            description = "The offer is selected",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseEntity.class)))
    @ApiResponse(
            responseCode = "400",
            description = "Fail to choose offer",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(
            responseCode = "404",
            description = "Application not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<HttpStatus> getOneOfTheOffers(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Selected loan offer",
            content = @Content(schema = @Schema(implementation = LoanOfferDTO.class)))
                                                        @RequestBody
                                                        @Valid LoanOfferDTO loanOfferDTO) {
        dealService.chooseOffer(loanOfferDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/calculate/{applicationId}")
    @Operation(summary = "Get loan parameters", description = "Allows to get all parameters for credit")
    @ApiResponse(
            responseCode = "200",
            description = "Parameters received!",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ResponseEntity.class)))
    @ApiResponse(
            responseCode = "400",
            description = "Scoring failed",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(
            responseCode = "404",
            description = "Application not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<HttpStatus> calculateCredit(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Finish registration request",
            content = @Content(schema = @Schema(implementation = FinishRegistrationRequestDTO.class)))
                                                      @RequestBody
                                                      @Valid FinishRegistrationRequestDTO finishRegistrationRequestDTO,
                                                      @Parameter(description = "Id of the application", required = true)
                                                      @PathVariable("applicationId") Long id) {
        dealService.calculateCredit(finishRegistrationRequestDTO, id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
