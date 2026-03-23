package com.invex.employees.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * The type Employee wrapper request.
 */
@Getter
@Setter
public class EmployeeWrapperRequest implements Serializable {

    @NotEmpty(message = "Employee list must not be empty")
    private List<@Valid EmployeeRequest> employees;
}
