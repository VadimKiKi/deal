package ru.taratonov.deal.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.taratonov.deal.dto.ApplicationStatusHistoryDTO;
import ru.taratonov.deal.dto.CreditDTO;
import ru.taratonov.deal.dto.EmploymentDTO;
import ru.taratonov.deal.dto.FinishRegistrationRequestDTO;
import ru.taratonov.deal.dto.LoanApplicationRequestDTO;
import ru.taratonov.deal.dto.LoanOfferDTO;
import ru.taratonov.deal.dto.PassportDTO;
import ru.taratonov.deal.dto.PaymentScheduleElement;
import ru.taratonov.deal.dto.ScoringDataDTO;
import ru.taratonov.deal.enums.ApplicationStatus;
import ru.taratonov.deal.enums.ChangeType;
import ru.taratonov.deal.enums.CreditStatus;
import ru.taratonov.deal.enums.Gender;
import ru.taratonov.deal.enums.MaritalStatus;
import ru.taratonov.deal.model.Application;
import ru.taratonov.deal.model.Client;
import ru.taratonov.deal.model.Credit;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FillingDataServiceTest {
    @Mock
    private LoanApplicationRequestDTO loanApplicationRequestDTO;
    @Mock
    private Client client;
    @Mock
    private Application application;
    @Mock
    private FinishRegistrationRequestDTO finishRegistrationRequestDTO;
    @Mock
    private CreditDTO creditDTO;
    @InjectMocks
    private FillingDataService fillingDataService;

    @Test
    void createClientOfRequest() {
        when(loanApplicationRequestDTO.getFirstName()).thenReturn("Vadim");
        when(loanApplicationRequestDTO.getLastName()).thenReturn("Tarat");
        when(loanApplicationRequestDTO.getMiddleName()).thenReturn("Nik");
        when(loanApplicationRequestDTO.getEmail()).thenReturn("vad.tar@example.com");
        when(loanApplicationRequestDTO.getBirthdate()).thenReturn(LocalDate.parse("1990-01-01"));
        when(loanApplicationRequestDTO.getPassportSeries()).thenReturn("AB");
        when(loanApplicationRequestDTO.getPassportNumber()).thenReturn("123456");

        Client result = fillingDataService.createClientOfRequest(loanApplicationRequestDTO);

        assertEquals("Vadim", result.getFirstName());
        assertEquals("Tarat", result.getLastName());
        assertEquals("Nik", result.getMiddleName());
        assertEquals("vad.tar@example.com", result.getEmail());
        assertEquals(LocalDate.parse("1990-01-01"), result.getBirthDate());
        assertEquals("AB", result.getPassportDTOId().getSeries());
        assertEquals("123456", result.getPassportDTOId().getNumber());
    }

    @Test
    void createApplicationOfRequest() {
        Application application = new Application()
                .setClient(client)
                .setStatus(ApplicationStatus.PREAPPROVAL)
                .setApplicationStatusHistoryDTO(List.of(new ApplicationStatusHistoryDTO()
                        .setStatus(ApplicationStatus.PREAPPROVAL)
                        .setTime(LocalDate.now())
                        .setChangeType(ChangeType.AUTOMATIC)))
                .setCreationDate(LocalDate.now());

        Application result = fillingDataService.createApplicationOfRequest(client);

        assertEquals(application, result);
    }

    @Test
    void fillAllInformationToScoringData() {
        EmploymentDTO employmentDTO = new EmploymentDTO();
        LoanOfferDTO loanOfferDTO = new LoanOfferDTO();
        PassportDTO passportDTO = new PassportDTO();
        when(application.getAppliedOffer()).thenReturn(loanOfferDTO);
        when(client.getPassportDTOId()).thenReturn(passportDTO);
        when(client.getFirstName()).thenReturn("Vadim");
        when(client.getLastName()).thenReturn("Tarat");
        when(client.getMiddleName()).thenReturn("Nik");
        when(finishRegistrationRequestDTO.getGender()).thenReturn(Gender.MALE);
        when(client.getBirthDate()).thenReturn(LocalDate.parse("1990-01-01"));
        when(finishRegistrationRequestDTO.getPassportIssueDate()).thenReturn(LocalDate.parse("1990-01-01"));
        when(finishRegistrationRequestDTO.getPassportIssueBranch()).thenReturn("a");
        when(finishRegistrationRequestDTO.getMaritalStatus()).thenReturn(MaritalStatus.SINGLE);
        when(finishRegistrationRequestDTO.getDependentAmount()).thenReturn(1);
        when(finishRegistrationRequestDTO.getEmployment()).thenReturn(employmentDTO);
        when(finishRegistrationRequestDTO.getAccount()).thenReturn("dd");


        ScoringDataDTO result = fillingDataService.fillAllInformationToScoringData(finishRegistrationRequestDTO, client, application);

        assertEquals("Vadim", result.getFirstName());
        assertEquals("Tarat", result.getLastName());
        assertEquals("Nik", result.getMiddleName());
        assertEquals(Gender.MALE, result.getGender());
        assertEquals(LocalDate.parse("1990-01-01"), result.getBirthdate());
        assertEquals("a", result.getPassportIssueBranch());
        assertEquals(LocalDate.parse("1990-01-01"), result.getPassportIssueDate());
        assertEquals(MaritalStatus.SINGLE, result.getMaritalStatus());
        assertEquals(1, result.getDependentAmount());
        assertEquals(employmentDTO, result.getEmployment());
        assertEquals("dd", result.getAccount());
    }

    @Test
    void createCreditAfterCalculating() {
        List<PaymentScheduleElement> list = new ArrayList<>();
        when(creditDTO.getAmount()).thenReturn(BigDecimal.ONE);
        when(creditDTO.getTerm()).thenReturn(1);
        when(creditDTO.getMonthlyPayment()).thenReturn(BigDecimal.ONE);
        when(creditDTO.getRate()).thenReturn(BigDecimal.ONE);
        when(creditDTO.getPsk()).thenReturn(BigDecimal.ONE);
        when(creditDTO.getPaymentSchedule()).thenReturn(list);
        when(creditDTO.getIsInsuranceEnabled()).thenReturn(Boolean.TRUE);
        when(creditDTO.getIsSalaryClient()).thenReturn(Boolean.TRUE);

        Credit result = fillingDataService.createCreditAfterCalculating(creditDTO, application);

        assertEquals(BigDecimal.ONE, result.getAmount());
        assertEquals(1, result.getTerm());
        assertEquals(BigDecimal.ONE, result.getMonthlyPayment());
        assertEquals(BigDecimal.ONE, result.getRate());
        assertEquals(BigDecimal.ONE, result.getPsk());
        assertEquals(list, result.getPaymentSchedule());
        assertEquals(Boolean.TRUE, result.getInsuranceEnable());
        assertEquals(Boolean.TRUE, result.getSalaryClient());
        assertEquals(CreditStatus.CALCULATED, result.getCreditStatus());
        assertEquals(application, result.getApplication());
    }

    @Test
    void fillAllDataOfClient() {
        PassportDTO passportDTO = new PassportDTO();
        EmploymentDTO employmentDTO = new EmploymentDTO();
        Client client1 = new Client();
        client1.setPassportDTOId(passportDTO);
        when(finishRegistrationRequestDTO.getPassportIssueDate()).thenReturn(LocalDate.parse("1990-01-01"));
        when(finishRegistrationRequestDTO.getPassportIssueBranch()).thenReturn("dd");
        when(finishRegistrationRequestDTO.getGender()).thenReturn(Gender.MALE);
        when(finishRegistrationRequestDTO.getMaritalStatus()).thenReturn(MaritalStatus.SINGLE);
        when(finishRegistrationRequestDTO.getDependentAmount()).thenReturn(1);
        when(finishRegistrationRequestDTO.getEmployment()).thenReturn(employmentDTO);
        when(finishRegistrationRequestDTO.getAccount()).thenReturn("11");

        Client result = fillingDataService.fillAllDataOfClient(client1, finishRegistrationRequestDTO);

        assertEquals(Gender.MALE, result.getGender());
        assertEquals(MaritalStatus.SINGLE, result.getMaritalStatus());
        assertEquals(1, result.getDependentAmount());
        assertEquals(employmentDTO, result.getEmploymentId());
        assertEquals(passportDTO, result.getPassportDTOId());
    }

    @Test
    void updateApplicationWithNewStatus() {
        Application application = new Application();
        ApplicationStatus applicationStatus = ApplicationStatus.APPROVED;

        List<ApplicationStatusHistoryDTO> applicationStatusHistoryDTO = new ArrayList<>();
        application.setApplicationStatusHistoryDTO(applicationStatusHistoryDTO);

        Application updatedApplication = fillingDataService.updateApplicationWithNewStatus(application, applicationStatus);

        assertEquals(1, updatedApplication.getApplicationStatusHistoryDTO().size());

        assertEquals(applicationStatus, updatedApplication.getStatus());
    }
}