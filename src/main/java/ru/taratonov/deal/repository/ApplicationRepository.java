package ru.taratonov.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.taratonov.deal.model.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
}
