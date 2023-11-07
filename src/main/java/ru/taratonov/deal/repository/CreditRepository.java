package ru.taratonov.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.taratonov.deal.model.Credit;

@Repository
public interface CreditRepository extends JpaRepository<Credit, Long> {
}
