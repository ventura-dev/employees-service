package com.invex.employees.dto;

import com.invex.employees.model.Sex;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * The type Employee response.
 */
@Getter
@Setter
public class EmployeeResponse implements Serializable {



    private Long id;
    private String firstName;
    private String secondName;
    private String lastNamePaternal;
    private String lastNameMaternal;
    private Integer age;
    private Sex sex;
    private LocalDate birthDate;
    private String position;
    private LocalDateTime createdAt;
    private Boolean active;
}