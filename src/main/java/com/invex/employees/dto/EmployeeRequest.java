package com.invex.employees.dto;

import com.invex.employees.model.Sex;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * The type Employee request.
 */
@Getter
@Setter
public class EmployeeRequest implements Serializable {
    @Size(max = 20, message = "First name must not exceed 50 characters")
    private String firstName;

    @Size(max = 20, message = "Second name must not exceed 50 characters")
    private String secondName;

    @Size(max = 20, message = "Last name paternal must not exceed 50 characters")
    private String lastNamePaternal;

    @Size(max = 20, message = "Last name maternal must not exceed 50 characters")
    private String lastNameMaternal;

    private Sex sex;

    @Past(message = "Birth date must be a past date")
    private LocalDate birthDate;

    @Size(max = 20, message = "Position must not exceed 100 characters")
    private String position;

    private Boolean active;
}
