package ru.taratonov.deal.service;

import com.google.common.base.Throwables;
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
import ru.taratonov.deal.exception.DatabaseException;
import ru.taratonov.deal.model.Application;
import ru.taratonov.deal.model.Client;
import ru.taratonov.deal.model.Credit;
import ru.taratonov.deal.repository.ApplicationRepository;
import ru.taratonov.deal.repository.ClientRepository;
import ru.taratonov.deal.repository.CreditRepository;

import java.sql.SQLException;
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

        log.info("Get loanApplicationRequestDTO and create new client with name - {}, surname - {}",
                loanApplicationRequestDTO.getFirstName(), loanApplicationRequestDTO.getLastName());
        Client client = fillingDataService.createClientOfRequest(loanApplicationRequestDTO);

        try {
            clientRepository.save(client);
        } catch (RuntimeException e) {
            Throwable rootCause = Throwables.getRootCause(e);
            if (rootCause instanceof SQLException) {
                if ("23505".equals(((SQLException) rootCause).getSQLState())) {
                    throw new DatabaseException(rootCause.getMessage());
                }
            }
        }
        log.debug("client {} {} is saved", client.getFirstName(), client.getLastName());


        Application application = fillingDataService.createApplicationOfRequest(client);
        application = applicationRepository.save(application);
        Long applicationId = application.getApplicationId();
        log.debug("application with id= {} is saved", applicationId);

        List<LoanOfferDTO> list = restTemplateRequestsService.requestToGetOffers(loanApplicationRequestDTO);

        list.forEach(loanOffer -> loanOffer.setApplicationId(applicationId));
        log.debug("application id assigned to each loanOffer - {}", applicationId);
        return list;
    }

    @Transactional
    public void chooseOffer(LoanOfferDTO loanOfferDTO) {
        Long applicationId = loanOfferDTO.getApplicationId();
        Optional<Application> foundApplication = applicationRepository.findById(applicationId);
        if (foundApplication.isEmpty()) {
            throw ApplicationNotFoundException.createWith(applicationId);
        }
        Application application = foundApplication.get();
        log.info("application with id= {} received", application.getApplicationId());
        application = fillingDataService.updateApplicationWhenChooseOffer(application, loanOfferDTO);
        applicationRepository.save(application);
        log.debug("application with id= {} is updated", application.getApplicationId());
    }

    @Transactional
    public void calculateCredit(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long id) {
        Optional<Application> foundApplication = applicationRepository.findById(id);
        if (foundApplication.isEmpty()) {
            throw ApplicationNotFoundException.createWith(id);
        }
        Application application = foundApplication.get();
        log.info("application with id= {} received", application.getApplicationId());
        Client client = application.getClient();
        log.debug("client {} {} received", client.getFirstName(), client.getLastName());

        ScoringDataDTO scoringDataDTO = fillingDataService
                .fillAllInformationToScoringData(finishRegistrationRequestDTO, client, application);
        log.debug("scoringDataDTO for {} {} is ready for calculating",
                scoringDataDTO.getFirstName(), scoringDataDTO.getLastName());

        client = fillingDataService.fillAllDataOfClient(client, finishRegistrationRequestDTO);
        clientRepository.save(client);
        log.debug("client {} {} is saved", client.getFirstName(), client.getLastName());

        CreditDTO creditDTO = restTemplateRequestsService.requestToCalculateCredit(scoringDataDTO);

        assert creditDTO != null : "creditDTO is null";
        Credit credit = fillingDataService.createCreditAfterCalculating(creditDTO, application);
        log.info("credit for {} {} with calculated",
                client.getFirstName(), client.getLastName());
        creditRepository.save(credit);
        log.debug("credit with id={} for application {} is saved",
                credit.getCreditId(), credit.getApplication().getApplicationId());

        application.setCredit(credit);
        applicationRepository.save(application);
        log.debug("application with id={} is saved", application.getApplicationId());
    }
}
