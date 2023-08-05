package ru.taratonov.deal.controller;

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
import ru.taratonov.deal.dto.FinishRegistrationRequestDTO;
import ru.taratonov.deal.dto.LoanApplicationRequestDTO;
import ru.taratonov.deal.dto.LoanOfferDTO;
import ru.taratonov.deal.service.DealService;

import java.util.List;

@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
public class DealController {

    private final DealService dealService;

    @PostMapping("/application")
    public List<LoanOfferDTO> getPossibleLoanOffers(@Valid @RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) {
        return dealService.getOffers(loanApplicationRequestDTO);
    }

    @PutMapping("/offer")
    public ResponseEntity<HttpStatus> getOneOfTheOffers(@Valid @RequestBody LoanOfferDTO loanOfferDTO) {
        dealService.chooseOffer(loanOfferDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/calculate/{applicationId}")
    public ResponseEntity<HttpStatus> calculateCredit(@Valid @RequestBody FinishRegistrationRequestDTO finishRegistrationRequestDTO,
                                                      @PathVariable("applicationId") Long id) {
        dealService.calculateCredit(finishRegistrationRequestDTO, id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
