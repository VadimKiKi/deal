package ru.taratonov.deal.service;

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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class RestTemplateRequestsService {
    private final RestTemplate restTemplate;

    @Value("${path.to.conveyor.get.offers}")
    private String PATH_TO_CONVEYOR_GET_OFFERS;

    @Value("${path.to.conveyor.calculate.credit}")
    private String PATH_TO_CONVEYOR_CALCULATE_CREDIT;

    @Autowired
    public RestTemplateRequestsService(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder
//                    .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }

    public List<LoanOfferDTO> requestToGetOffers(LoanApplicationRequestDTO loanApplicationRequestDTO){
        ResponseEntity<LoanOfferDTO[]> responseEntity =
                restTemplate.postForEntity(PATH_TO_CONVEYOR_GET_OFFERS, loanApplicationRequestDTO, LoanOfferDTO[].class);
        return Arrays.stream(Objects.requireNonNull(responseEntity.getBody())).toList();
    }

    public CreditDTO requestToCalculateCredit(ScoringDataDTO scoringDataDTO){
        ResponseEntity<CreditDTO> creditDTOResponseEntity =
                restTemplate.postForEntity(PATH_TO_CONVEYOR_CALCULATE_CREDIT, scoringDataDTO, CreditDTO.class);
        return Objects.requireNonNull(creditDTOResponseEntity.getBody());
    }
}



