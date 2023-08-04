package ru.taratonov.deal.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.taratonov.deal.dto.LoanOfferDTO;
import ru.taratonov.deal.enums.ApplicationStatus;
import ru.taratonov.deal.model.jsonb.StatusHistory;

import java.time.LocalDate;

@Entity
@Data
public class Application {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "application_id")
    private Long applicationId;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "client_id")
    @JsonBackReference(value = "client_application")
    private Client client;

//    @Column(name = "credit_id")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credit_id", referencedColumnName = "credit_id")
    private Credit credit;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "applied_offer")
    @JdbcTypeCode(SqlTypes.JSON)
    private LoanOfferDTO appliedOffer;

    @Column(name = "sign_date")
    private LocalDate signDate;

    @Column(name = "ses_code")
    private Integer sesCode;

    @Column(name = "status_history")
    @JdbcTypeCode(SqlTypes.JSON)
    private StatusHistory statusHistory;

}
