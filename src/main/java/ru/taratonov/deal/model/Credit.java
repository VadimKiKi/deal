package ru.taratonov.deal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.taratonov.deal.dto.PaymentScheduleElement;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
public class Credit {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "credit_id")
    private Long creditId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "term")
    private Integer term;

    @Column(name = "monthly_payment")
    private BigDecimal monthlyPayment;

    @Column(name = "rate")
    private BigDecimal rate;

    @Column(name = "psk")
    private BigDecimal psk;

    @Column(name = "payment_schedule")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<PaymentScheduleElement> paymentSchedule;

    @Column(name = "insurance_enable")
    private Boolean insuranceEnable;

    @Column(name = "salary_client")
    private Boolean salaryClient;

    @Column(name = "credit_status")
    private String creditStatus;

    @OneToOne(mappedBy = "credit")
    private Application application;
}
