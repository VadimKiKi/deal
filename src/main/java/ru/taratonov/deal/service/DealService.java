package ru.taratonov.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Slf4j
public class DealService {

    private final ClientRepository clientRepository;
    private final ApplicationRepository applicationRepository;
    private final CreditRepository creditRepository;
    private final RestTemplateRequestsService restTemplateRequestsService;
    private final FillingDataService fillingDataService;

    @Transactional
    public List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO loanApplicationRequestDTO) {

        log.info("Get loanApplicationRequestDTO and create new client");
        Client client = fillingDataService.createClientOfRequest(loanApplicationRequestDTO);
        clientRepository.save(client);
        log.debug("client is saved");


        Application application = fillingDataService.createApplicationOfRequest(client);
        application = applicationRepository.save(application);
        log.debug("application is saved");

        List<LoanOfferDTO> list = restTemplateRequestsService.requestToGetOffers(loanApplicationRequestDTO);
        Long applicationId = application.getApplicationId();
        list.forEach(l -> l.setApplicationId(applicationId));
        log.debug("application id assigned");
        return list;
    }

    @Transactional
    public void chooseOffer(LoanOfferDTO loanOfferDTO) {
        Long applicationId = loanOfferDTO.getApplicationId();
        Optional<Application> foundApplication = applicationRepository.findById(applicationId);
        if (foundApplication.isEmpty()) {
            throw ApplicationNotFoundException.createWith(applicationId);
        }
        log.info("application received");
        Application application = foundApplication.get();
        application = fillingDataService.updateApplicationWhenChooseOffer(application, loanOfferDTO);
        applicationRepository.save(application);
        log.debug("application is saved");
    }

    @Transactional
    public void calculateCredit(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long id) {
        Optional<Application> foundApplication = applicationRepository.findById(id);
        if (foundApplication.isEmpty()) {
            throw ApplicationNotFoundException.createWith(id);
        }
        Application application = foundApplication.get();
        log.info("application received");
        Client client = application.getClient();
        log.debug("client received");

        ScoringDataDTO scoringDataDTO = fillingDataService
                .fillAllInformationToScoringData(finishRegistrationRequestDTO, client, application);
        log.debug("scoringDataDTO is ready for calculating");

        client = fillingDataService.fillAllDataOfClient(client, finishRegistrationRequestDTO);
        clientRepository.save(client);
        log.debug("client is saved");

        CreditDTO creditDTO = restTemplateRequestsService.requestToCalculateCredit(scoringDataDTO);

        assert creditDTO != null;
        Credit credit = fillingDataService.createCreditAfterCalculating(creditDTO, application);
        log.info("credit calculated");
        creditRepository.save(credit);
        log.debug("credit is saved");

        application.setCredit(credit);
        applicationRepository.save(application);
        log.debug("application is saved");
    }
}
