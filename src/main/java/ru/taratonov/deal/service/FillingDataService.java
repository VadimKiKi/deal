package ru.taratonov.deal.service;

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
import ru.taratonov.deal.model.jsonb.Passport;

import java.time.LocalDate;
import java.util.List;

@Service
public class FillingDataService {

    public Client createClientOfRequest(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        return new Client()
                .setFirstName(loanApplicationRequestDTO.getFirstName())
                .setLastName(loanApplicationRequestDTO.getLastName())
                .setMiddleName(loanApplicationRequestDTO.getMiddleName())
                .setEmail(loanApplicationRequestDTO.getEmail())
                .setBirthDate(loanApplicationRequestDTO.getBirthdate())
                .setPassportId(new Passport()
                        .setSeries(loanApplicationRequestDTO.getPassportSeries())
                        .setNumber(loanApplicationRequestDTO.getPassportNumber()));

    }

    public Application createApplicationOfRequest(Client client) {
        return new Application()
                .setClient(client)
                .setCreationDate(LocalDate.now());
    }

    public Application updateApplicationWhenChooseOffer(Application application, LoanOfferDTO loanOfferDTO) {
        application
                .setStatus(ApplicationStatus.PREAPPROVAL)
                .setApplicationStatusHistoryDTO(List.of(new ApplicationStatusHistoryDTO()
                        .setStatus(ApplicationStatus.PREAPPROVAL)
                        .setTime(LocalDate.now())
                        .setChangeType(ChangeType.AUTOMATIC)))
                .setAppliedOffer(loanOfferDTO);
        return application;
    }

    public ScoringDataDTO fillAllInformationToScoringData(FinishRegistrationRequestDTO finishRegistrationRequestDTO,
                                                          Client client, Application application) {
        return new ScoringDataDTO()
                .setAmount(application.getAppliedOffer().getTotalAmount())
                .setTerm(application.getAppliedOffer().getTerm())
                .setFirstName(client.getFirstName())
                .setLastName(client.getLastName())
                .setMiddleName(client.getMiddleName())
                .setGender(finishRegistrationRequestDTO.getGender())
                .setBirthdate(client.getBirthDate())
                .setPassportSeries(client.getPassportId().getSeries())
                .setPassportNumber(client.getPassportId().getNumber())
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
        Passport newPassport = client.getPassportId()
                .setIssueDate(finishRegistrationRequestDTO.getPassportIssueDate())
                .setIssueBranch(finishRegistrationRequestDTO.getPassportIssueBranch());
        client
                .setGender(finishRegistrationRequestDTO.getGender())
                .setMaritalStatus(finishRegistrationRequestDTO.getMaritalStatus())
                .setDependentAmount(finishRegistrationRequestDTO.getDependentAmount())
                .setEmploymentId(finishRegistrationRequestDTO.getEmployment())
                .setAccount(finishRegistrationRequestDTO.getAccount())
                .setPassportId(newPassport);
        return client;
    }
}
