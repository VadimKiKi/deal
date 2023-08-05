package ru.taratonov.deal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.taratonov.deal.dto.CreditDTO;
import ru.taratonov.deal.dto.FinishRegistrationRequestDTO;
import ru.taratonov.deal.dto.LoanApplicationRequestDTO;
import ru.taratonov.deal.dto.LoanOfferDTO;
import ru.taratonov.deal.dto.ScoringDataDTO;
import ru.taratonov.deal.exception.ApplicationNotFoundException;
import ru.taratonov.deal.model.Application;
import ru.taratonov.deal.model.Client;
import ru.taratonov.deal.model.Credit;
import ru.taratonov.deal.repository.ApplicationRepository;
import ru.taratonov.deal.repository.ClientRepository;
import ru.taratonov.deal.repository.CreditRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DealService {

    private final ClientRepository clientRepository;
    private final ApplicationRepository applicationRepository;
    private final CreditRepository creditRepository;
    private final RestTemplateRequestsService restTemplateRequestsService;
    private final FillingDataService fillingDataService;

    public List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO loanApplicationRequestDTO) {

        Client client = fillingDataService.createClientOfRequest(loanApplicationRequestDTO);
        clientRepository.save(client);

        Application application = fillingDataService.createApplicationOfRequest(client);
        application = applicationRepository.save(application);

        List<LoanOfferDTO> list = restTemplateRequestsService.requestToGetOffers(loanApplicationRequestDTO);
        Long applicationId = application.getApplicationId();
        list.forEach(l -> l.setApplicationId(applicationId));
        return list;
    }

    public void chooseOffer(LoanOfferDTO loanOfferDTO) {
        Long applicationId = loanOfferDTO.getApplicationId();
        Optional<Application> foundApplication = applicationRepository.findById(applicationId);
        if (foundApplication.isEmpty()) {
            throw ApplicationNotFoundException.createWith(applicationId);
        }

        Application application = foundApplication.get();
        application = fillingDataService.updateApplicationWhenChooseOffer(application, loanOfferDTO);
        applicationRepository.save(application);
    }

    public void calculateCredit(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long id) {
        Optional<Application> foundApplication = applicationRepository.findById(id);
        if (foundApplication.isEmpty()) {
            throw ApplicationNotFoundException.createWith(id);
        }
        Application application = foundApplication.get();
        Client client = application.getClient();

        ScoringDataDTO scoringDataDTO = fillingDataService
                .fillAllInformationToScoringData(finishRegistrationRequestDTO, client, application);

        client = fillingDataService.fillAllDataOfClient(client, finishRegistrationRequestDTO);
        clientRepository.save(client);

        CreditDTO creditDTO = restTemplateRequestsService.requestToCalculateCredit(scoringDataDTO);

        assert creditDTO != null;
        Credit credit = fillingDataService.createCreditAfterCalculating(creditDTO, application);
        creditRepository.save(credit);

        application.setCredit(credit);
        applicationRepository.save(application);
    }
}
