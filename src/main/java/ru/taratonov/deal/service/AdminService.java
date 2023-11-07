package ru.taratonov.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.taratonov.deal.exception.ApplicationNotFoundException;
import ru.taratonov.deal.model.Application;
import ru.taratonov.deal.repository.ApplicationRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final ApplicationRepository applicationRepository;
    public Application getApplicationById(Long id) {
        Optional<Application> foundApplication = applicationRepository.findById(id);
        if (foundApplication.isEmpty()) {
            throw ApplicationNotFoundException.createWith(id);
        }
        Application application = foundApplication.get();
        log.info("Application with id={} receive", id);
        return application;
    }

    public List<Application> getAllApplications() {
        List<Application> all = applicationRepository.findAll();
        log.info("Applications receive");
        return all;
    }
}
