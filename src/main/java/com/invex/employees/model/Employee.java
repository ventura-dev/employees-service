package com.invex.employees.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * The type Employee.
 */
@Entity
@Table(name="employee")
@Getter
@Setter
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "second_name", length = 50)
    private String secondName;

    @Column(name = "last_name_paternal", nullable = false, length = 50)
    private String lastNamePaternal;

    @Column(name = "last_name_maternal", length = 50)
    private String lastNameMaternal;

    @Column(nullable = false)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Sex sex;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false, length = 100)
    private String position;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false,
            insertable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
    )
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean active;

    /**
     * Instantiates a new Employee.
     */
    public Employee() {
    }

    /**
     * Pre persist.
     */
    @PrePersist
    public void prePersist() {
        if (this.active == null) {
            this.active = true;
        }
    }


}
