package ru.taratonov.deal.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.taratonov.deal.dto.EmploymentDTO;
import ru.taratonov.deal.dto.PassportDTO;
import ru.taratonov.deal.enums.Gender;
import ru.taratonov.deal.enums.MaritalStatus;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "client")
@NoArgsConstructor
@Accessors(chain = true)
public class Client {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "first_name")
    @NonNull
    private String firstName;

    @Column(name = "last_name")
    @NonNull
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "email")
    private String email;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "marital_status")
    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;

    @Column(name = "dependent_amount")
    private Integer dependentAmount;

    @Column(name = "passport_id")
    @JdbcTypeCode(SqlTypes.JSON)
    private PassportDTO passportDTOId;

    @Column(name = "employment_id")
    @JdbcTypeCode(SqlTypes.JSON)
    private EmploymentDTO employmentId;

    @Column(name = "account")
    private String account;

    @OneToOne(mappedBy = "client")
    @JsonManagedReference(value = "client_application")
    private Application application;
}
