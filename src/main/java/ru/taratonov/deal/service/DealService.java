package ru.taratonov.deal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.taratonov.deal.dto.LoanApplicationRequestDTO;
import ru.taratonov.deal.dto.LoanOfferDTO;
import ru.taratonov.deal.repository.ClientRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DealService {

    private final ClientRepository clientRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        ResponseEntity<LoanOfferDTO[]> responseEntity = restTemplate.postForEntity("http://localhost:8080/conveyor/offers", loanApplicationRequestDTO, LoanOfferDTO[].class);
        List<LoanOfferDTO> list = Arrays.stream(Objects.requireNonNull(responseEntity.getBody())).toList();
        return list;
    }
}
