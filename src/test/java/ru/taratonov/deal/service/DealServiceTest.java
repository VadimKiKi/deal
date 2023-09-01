package ru.taratonov.deal.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.taratonov.deal.dto.ApplicationDTO;
import ru.taratonov.deal.dto.CreditDTO;
import ru.taratonov.deal.dto.FinishRegistrationRequestDTO;
import ru.taratonov.deal.dto.LoanApplicationRequestDTO;
import ru.taratonov.deal.dto.LoanOfferDTO;
import ru.taratonov.deal.dto.ScoringDataDTO;
import ru.taratonov.deal.enums.ApplicationStatus;
import ru.taratonov.deal.exception.ApplicationNotFoundException;
import ru.taratonov.deal.model.Application;
import ru.taratonov.deal.model.Client;
import ru.taratonov.deal.model.Credit;
import ru.taratonov.deal.repository.ApplicationRepository;
import ru.taratonov.deal.repository.ClientRepository;
import ru.taratonov.deal.repository.CreditRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DealServiceTest {
    @Mock
    private FillingDataService fillingDataService;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private CreditRepository creditRepository;
    @Mock
    private RestTemplateRequestsService restTemplateRequestsService;
    @InjectMocks
    private DealService dealService;

    @Test
    void getOffers() {
        LoanApplicationRequestDTO loanApplicationRequestDTO = new LoanApplicationRequestDTO();
        Client client = new Client();
        Application application = new Application();
        List<LoanOfferDTO> loanOffers = List.of(new LoanOfferDTO(), new LoanOfferDTO());

        when(fillingDataService.createClientOfRequest(loanApplicationRequestDTO)).thenReturn(client);
        when(fillingDataService.createApplicationOfRequest(client)).thenReturn(application);
        when(applicationRepository.save(application)).thenReturn(application);
        when(restTemplateRequestsService.requestToGetOffers(loanApplicationRequestDTO)).thenReturn(loanOffers);

        List<LoanOfferDTO> result = dealService.getOffers(loanApplicationRequestDTO);

        assertEquals(loanOffers.size(), result.size());
        verify(fillingDataService, times(1)).createClientOfRequest(loanApplicationRequestDTO);
        verify(clientRepository, times(1)).save(client);
        verify(fillingDataService, times(1)).createApplicationOfRequest(client);
        verify(applicationRepository, times(1)).save(application);
        verify(restTemplateRequestsService, times(1)).requestToGetOffers(loanApplicationRequestDTO);
    }

    @Test
    void chooseOffer_shouldThrowExceptionWhenApplicationNotFound() {
        Long applicationId = 1L;
        LoanOfferDTO loanOfferDTO = new LoanOfferDTO()
                .setApplicationId(applicationId);

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.empty());

        assertThrows(ApplicationNotFoundException.class, () -> dealService.chooseOffer(loanOfferDTO));

        verify(applicationRepository, times(1)).findById(applicationId);
        verifyNoMoreInteractions(applicationRepository);
    }

    @Test
    void chooseOffer_shouldSaveApplicationWhenFound() {
        Long applicationId = 1L;
        LoanOfferDTO loanOfferDTO = new LoanOfferDTO()
                .setApplicationId(applicationId);

        Application application = new Application();

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(application));
        when(fillingDataService.updateApplicationWhenChooseOffer(application, loanOfferDTO)).thenReturn(application);

        dealService.chooseOffer(loanOfferDTO);

        verify(applicationRepository, times(1)).findById(applicationId);
        verify(fillingDataService, times(1)).updateApplicationWhenChooseOffer(application, loanOfferDTO);
        verify(applicationRepository, times(1)).save(application);
        verifyNoMoreInteractions(applicationRepository);
        verifyNoMoreInteractions(fillingDataService);
    }


    @Test
    void calculateCredit_shouldThrowExceptionWhenApplicationNotFound() {
        Long applicationId = 1L;
        FinishRegistrationRequestDTO finishRegistrationRequestDTO = new FinishRegistrationRequestDTO();

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.empty());

        assertThrows(ApplicationNotFoundException.class,
                () -> dealService.calculateCredit(finishRegistrationRequestDTO, applicationId));

        verify(applicationRepository, times(1)).findById(applicationId);
        verifyNoMoreInteractions(applicationRepository);
    }

    @Test
    void calculateCredit() {
        FinishRegistrationRequestDTO finishRegistrationRequestDTO = new FinishRegistrationRequestDTO();
        Long id = 1L;
        Client client = new Client();
        Application application = new Application().setClient(client);
        ScoringDataDTO scoringDataDTO = new ScoringDataDTO();
        CreditDTO creditDTO = new CreditDTO();
        Credit credit = new Credit();

        when(applicationRepository.findById(id)).thenReturn(Optional.of(application));
        when(fillingDataService.fillAllInformationToScoringData(finishRegistrationRequestDTO, client, application))
                .thenReturn(scoringDataDTO);
        when(fillingDataService.fillAllDataOfClient(client, finishRegistrationRequestDTO)).thenReturn(client);
        when(restTemplateRequestsService.requestToCalculateCredit(scoringDataDTO)).thenReturn(creditDTO);
        when(fillingDataService.createCreditAfterCalculating(creditDTO, application)).thenReturn(credit);
        when(fillingDataService.updateApplicationWithNewStatus(eq(application), any(ApplicationStatus.class))).thenReturn(application);

        dealService.calculateCredit(finishRegistrationRequestDTO, id);

        verify(applicationRepository, times(1)).findById(id);
        verify(fillingDataService, times(1))
                .fillAllInformationToScoringData(finishRegistrationRequestDTO, client, application);
        verify(fillingDataService, times(1)).fillAllDataOfClient(client, finishRegistrationRequestDTO);
        verify(clientRepository, times(1)).save(client);
        verify(restTemplateRequestsService, times(1)).requestToCalculateCredit(scoringDataDTO);
        verify(fillingDataService, times(1)).createCreditAfterCalculating(creditDTO, application);
        verify(creditRepository, times(1)).save(credit);
        verify(applicationRepository, times(1)).save(application);
    }

    @Test
    void getApplicationDTOById() {
        Long id = 1L;
        Client client = new Client()
                .setFirstName("")
                .setMiddleName("")
                .setLastName("");
        Credit credit = new Credit()
                .setAmount(BigDecimal.ONE)
                .setTerm(0)
                .setMonthlyPayment(BigDecimal.ONE)
                .setRate(BigDecimal.ONE)
                .setPsk(BigDecimal.ONE)
                .setPaymentSchedule(new ArrayList<>())
                .setInsuranceEnable(Boolean.TRUE)
                .setSalaryClient(Boolean.TRUE);
        Application application = new Application();
        application.setApplicationId(id)
                .setClient(client)
                .setCredit(credit)
                .setCreationDate(LocalDate.MIN)
                .setSignDate(LocalDate.MIN)
                .setSesCode(1111);

        when(applicationRepository.findById(id)).thenReturn(Optional.of(application));

        ApplicationDTO expectedDTO = new ApplicationDTO();
        expectedDTO.setApplicationId(application.getApplicationId())
                .setFirstName(application.getClient().getFirstName())
                .setLastName(application.getClient().getLastName())
                .setMiddleName(application.getClient().getMiddleName())
                .setAmount(application.getCredit().getAmount())
                .setTerm(application.getCredit().getTerm())
                .setMonthlyPayment(application.getCredit().getMonthlyPayment())
                .setRate(application.getCredit().getRate())
                .setPsk(application.getCredit().getPsk())
                .setPaymentSchedule(application.getCredit().getPaymentSchedule())
                .setInsuranceEnable(application.getCredit().getInsuranceEnable())
                .setSalaryClient(application.getCredit().getSalaryClient())
                .setCreationDate(application.getCreationDate())
                .setSignDate(application.getSignDate())
                .setSesCode(application.getSesCode());

        ApplicationDTO actualDTO = dealService.getApplicationDTOById(id);

        assertEquals(expectedDTO, actualDTO);
    }
}