package ru.taratonov.deal.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.taratonov.deal.exception.ApplicationNotFoundException;
import ru.taratonov.deal.model.Application;
import ru.taratonov.deal.repository.ApplicationRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DocumentKafkaServiceTest {
    @InjectMocks
    private DocumentKafkaService documentKafkaService;
    @Mock
    private ApplicationRepository applicationRepository;

    @Test
    void getApplication_containsInDb() {
        Application application = new Application();
        when(applicationRepository.findById(any(Long.class))).thenReturn(Optional.of(application));

        Application result = documentKafkaService.getApplication(any(Long.class));

        assertEquals(application, result);
    }

    @Test
    void getApplication_notFound() {
        when(applicationRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(ApplicationNotFoundException.class, () -> documentKafkaService.getApplication(any(Long.class)));
    }
}