package ru.taratonov.deal.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.taratonov.deal.annotation.ToAudit;
import ru.taratonov.deal.dto.EmailMessageDTO;
import ru.taratonov.deal.enums.ApplicationStatus;
import ru.taratonov.deal.enums.CreditStatus;
import ru.taratonov.deal.enums.Theme;
import ru.taratonov.deal.exception.ApplicationNotFoundException;
import ru.taratonov.deal.model.Application;
import ru.taratonov.deal.model.Credit;
import ru.taratonov.deal.repository.ApplicationRepository;
import ru.taratonov.deal.repository.CreditRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentKafkaService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final ApplicationRepository applicationRepository;
    private final CreditRepository creditRepository;
    private final FillingDataService fillingDataService;

    public void sendMessage(Application application, Theme theme) {
        EmailMessageDTO emailMessageDTO = new EmailMessageDTO()
                .setEmailAddress(application.getClient().getEmail())
                .setTheme(theme)
                .setApplicationId(application.getApplicationId());
        log.debug("EmailMessageDTO is ready to sending {}", emailMessageDTO);
        try {
            sendMessageToKafka(emailMessageDTO);
        } catch (JsonProcessingException e) {
            log.error("error when send message to kafka");
            throw new RuntimeException(e);
        }
    }

    @ToAudit
    public void sendDocuments(Long id) {
        Application application = getApplication(id);
        application = fillingDataService.updateApplicationWithNewStatus(application, ApplicationStatus.PREPARE_DOCUMENTS);
        application = applicationRepository.save(application);
        sendMessage(application, Theme.SEND_DOCUMENTS);
        application = fillingDataService.updateApplicationWithNewStatus(application, ApplicationStatus.DOCUMENT_CREATED);
        applicationRepository.save(application);
    }

    public void requestSignDocument(Long id) {
        Application application = getApplication(id);
        Integer sesCode = generateSesCode();
        application.setSesCode(sesCode);
        application = applicationRepository.save(application);
        sendMessage(application, Theme.SEND_SES);
    }

    @ToAudit
    public void signDocument(Long id, Integer sesCode) {
        Application application = getApplication(id);
        int realCode = application.getSesCode();
        if (sesCode == realCode) {
            application = fillingDataService.updateApplicationWithNewStatus(application, ApplicationStatus.DOCUMENT_SIGNED);
            application = applicationRepository.save(application);
            sendMessage(application, Theme.CREDIT_ISSUED);
            application = fillingDataService.updateApplicationWithNewStatus(application, ApplicationStatus.CREDIT_ISSUED);
            application.setSignDate(LocalDate.now());
            Credit credit = application.getCredit();
            credit.setCreditStatus(CreditStatus.ISSUED);
            log.info("status of credit was change to issued");
            creditRepository.save(credit);
            applicationRepository.save(application);
        } else {
            fillingDataService.updateApplicationWithNewStatus(application, ApplicationStatus.CLIENT_DENIED);
            application = applicationRepository.save(application);
            sendMessage(application, Theme.APPLICATION_DENIED);
        }
    }

    @ToAudit
    public void denyApplication(Long id) {
        Application application = getApplication(id);
        fillingDataService.updateApplicationWithNewStatus(application, ApplicationStatus.CLIENT_DENIED);
        application = applicationRepository.save(application);
        sendMessage(application, Theme.APPLICATION_DENIED);
    }

    public void sendMessageToKafka(EmailMessageDTO emailMessageDTO) throws JsonProcessingException {
        String message = objectMapper.writeValueAsString(emailMessageDTO);
        kafkaTemplate.send(emailMessageDTO.getTheme().getTitle(), message);
        log.info("message send to kafka with data {} to topic {}", message, emailMessageDTO.getTheme().getTitle());
    }

    public Application getApplication(Long id) {
        Optional<Application> foundApplication = applicationRepository.findById(id);
        if (foundApplication.isEmpty()) {
            throw ApplicationNotFoundException.createWith(id);
        }
        Application application = foundApplication.get();
        log.info("application with id= {} received", application.getApplicationId());
        return application;
    }

    public Integer generateSesCode() {
        Random random = new Random();
        int first = random.nextInt(9) + 1;
        int second = random.nextInt(9) + 1;
        int third = random.nextInt(9) + 1;
        int fourth = random.nextInt(9) + 1;
        int sesCode = first * 1000 + second * 100 + third * 10 + fourth;
        log.info("ses code: {} created", sesCode);
        return sesCode;
    }


}
