package ru.taratonov.deal.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.taratonov.deal.dto.ApplicationStatusHistoryDTO;
import ru.taratonov.deal.dto.CreditDTO;
import ru.taratonov.deal.dto.FinishRegistrationRequestDTO;
import ru.taratonov.deal.dto.LoanApplicationRequestDTO;
import ru.taratonov.deal.dto.LoanOfferDTO;
import ru.taratonov.deal.dto.ScoringDataDTO;
import ru.taratonov.deal.enums.ApplicationStatus;
import ru.taratonov.deal.enums.ChangeType;
import ru.taratonov.deal.enums.CreditStatus;
import ru.taratonov.deal.model.Application;
import ru.taratonov.deal.model.Client;
import ru.taratonov.deal.model.Credit;
import ru.taratonov.deal.dto.PassportDTO;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class FillingDataService {

    public Client createClientOfRequest(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.debug("Start create new client with data from {}", loanApplicationRequestDTO);
        return new Client()
                .setFirstName(loanApplicationRequestDTO.getFirstName())
                .setLastName(loanApplicationRequestDTO.getLastName())
                .setMiddleName(loanApplicationRequestDTO.getMiddleName())
                .setEmail(loanApplicationRequestDTO.getEmail())
                .setBirthDate(loanApplicationRequestDTO.getBirthdate())
                .setPassportDTOId(new PassportDTO()
                        .setSeries(loanApplicationRequestDTO.getPassportSeries())
                        .setNumber(loanApplicationRequestDTO.getPassportNumber()));

    }

    public Application createApplicationOfRequest(Client client) {
        log.debug("Start create new application with {}", client);
        return new Application()
                .setClient(client)
                .setStatus(ApplicationStatus.PREAPPROVAL)
                .setApplicationStatusHistoryDTO(List.of(new ApplicationStatusHistoryDTO()
                        .setStatus(ApplicationStatus.PREAPPROVAL)
                        .setTime(LocalDate.now())
                        .setChangeType(ChangeType.AUTOMATIC)))
                .setCreationDate(LocalDate.now());
    }

    public Application updateApplicationWhenChooseOffer(Application application, LoanOfferDTO loanOfferDTO) {
        log.debug("Start update application with {} and {}", application, loanOfferDTO);
        List<ApplicationStatusHistoryDTO> applicationStatusHistoryDTO = application.getApplicationStatusHistoryDTO();
        applicationStatusHistoryDTO.add(new ApplicationStatusHistoryDTO()
                .setStatus(ApplicationStatus.APPROVED)
                .setTime(LocalDate.now())
                .setChangeType(ChangeType.AUTOMATIC));

        application
                .setAppliedOffer(loanOfferDTO)
                .setStatus(ApplicationStatus.APPROVED)
                .setApplicationStatusHistoryDTO(applicationStatusHistoryDTO);
        return application;
    }

    public ScoringDataDTO fillAllInformationToScoringData(FinishRegistrationRequestDTO finishRegistrationRequestDTO,
                                                          Client client, Application application) {
        log.debug("Start filling scoringData with {}, {} and {}", finishRegistrationRequestDTO, client, application);
        return new ScoringDataDTO()
                .setAmount(application.getAppliedOffer().getTotalAmount())
                .setTerm(application.getAppliedOffer().getTerm())
                .setFirstName(client.getFirstName())
                .setLastName(client.getLastName())
                .setMiddleName(client.getMiddleName())
                .setGender(finishRegistrationRequestDTO.getGender())
                .setBirthdate(client.getBirthDate())
                .setPassportSeries(client.getPassportDTOId().getSeries())
                .setPassportNumber(client.getPassportDTOId().getNumber())
                .setPassportIssueDate(finishRegistrationRequestDTO.getPassportIssueDate())
                .setPassportIssueBranch(finishRegistrationRequestDTO.getPassportIssueBranch())
                .setMaritalStatus(finishRegistrationRequestDTO.getMaritalStatus())
                .setDependentAmount(finishRegistrationRequestDTO.getDependentAmount())
                .setEmployment(finishRegistrationRequestDTO.getEmployment())
                .setAccount(finishRegistrationRequestDTO.getAccount())
                .setIsInsuranceEnabled(application.getAppliedOffer().getIsInsuranceEnabled())
                .setIsSalaryClient(application.getAppliedOffer().getIsSalaryClient());
    }

    public Credit createCreditAfterCalculating(CreditDTO creditDTO, Application application){
        log.debug("Start create credit with {} and {}", creditDTO, application);
        return new Credit()
                .setAmount(creditDTO.getAmount())
                .setTerm(creditDTO.getTerm())
                .setMonthlyPayment(creditDTO.getMonthlyPayment())
                .setRate(creditDTO.getRate())
                .setPsk(creditDTO.getPsk())
                .setPaymentSchedule(creditDTO.getPaymentSchedule())
                .setInsuranceEnable(creditDTO.getIsInsuranceEnabled())
                .setSalaryClient(creditDTO.getIsSalaryClient())
                .setApplication(application)
                .setCreditStatus(CreditStatus.CALCULATED);
    }

    public Client fillAllDataOfClient(Client client, FinishRegistrationRequestDTO finishRegistrationRequestDTO){
        log.debug("Start update client with {} and {}", client, finishRegistrationRequestDTO);
        PassportDTO newPassportDTO = client.getPassportDTOId()
                .setIssueDate(finishRegistrationRequestDTO.getPassportIssueDate())
                .setIssueBranch(finishRegistrationRequestDTO.getPassportIssueBranch());
        client
                .setGender(finishRegistrationRequestDTO.getGender())
                .setMaritalStatus(finishRegistrationRequestDTO.getMaritalStatus())
                .setDependentAmount(finishRegistrationRequestDTO.getDependentAmount())
                .setEmploymentId(finishRegistrationRequestDTO.getEmployment())
                .setAccount(finishRegistrationRequestDTO.getAccount())
                .setPassportDTOId(newPassportDTO);
        return client;
    }

    public Application updateApplicationWithNewStatus(Application application, ApplicationStatus applicationStatus){
        log.debug("Start update application with new status {} ", applicationStatus);
        List<ApplicationStatusHistoryDTO> applicationStatusHistoryDTO = application.getApplicationStatusHistoryDTO();
        applicationStatusHistoryDTO.add(new ApplicationStatusHistoryDTO()
                .setTime(LocalDate.now())
                .setChangeType(ChangeType.AUTOMATIC)
                .setStatus(applicationStatus));
        application.setApplicationStatusHistoryDTO(applicationStatusHistoryDTO);
        application.setStatus(applicationStatus);
        return application;
    }
}
