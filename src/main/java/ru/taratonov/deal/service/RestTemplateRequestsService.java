package ru.taratonov.deal.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.taratonov.deal.dto.CreditDTO;
import ru.taratonov.deal.dto.LoanApplicationRequestDTO;
import ru.taratonov.deal.dto.LoanOfferDTO;
import ru.taratonov.deal.dto.ScoringDataDTO;
import ru.taratonov.deal.util.DealResponseErrorHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class RestTemplateRequestsService {
    private final RestTemplate restTemplate;

    @Value("${custom.integration.conveyor.get.offers}")
    private String PATH_TO_CONVEYOR_GET_OFFERS;

    @Value("${custom.integration.conveyor.calculate.credit}")
    private String PATH_TO_CONVEYOR_CALCULATE_CREDIT;

    @Autowired
    public RestTemplateRequestsService(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder
                .errorHandler(new DealResponseErrorHandler())
                .build();
    }

    public List<LoanOfferDTO> requestToGetOffers(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("request to get offer to conveyor with {}", loanApplicationRequestDTO);
        ResponseEntity<LoanOfferDTO[]> responseEntity =
                restTemplate.postForEntity(PATH_TO_CONVEYOR_GET_OFFERS, loanApplicationRequestDTO, LoanOfferDTO[].class);
        return Arrays.stream(Objects.requireNonNull(responseEntity.getBody())).toList();
    }

    public CreditDTO requestToCalculateCredit(ScoringDataDTO scoringDataDTO) {
        log.info("request to calculate credit to conveyor with {}", scoringDataDTO);
        ResponseEntity<CreditDTO> creditDTOResponseEntity =
                restTemplate.postForEntity(PATH_TO_CONVEYOR_CALCULATE_CREDIT, scoringDataDTO, CreditDTO.class);
        return Objects.requireNonNull(creditDTOResponseEntity.getBody());
    }
}



