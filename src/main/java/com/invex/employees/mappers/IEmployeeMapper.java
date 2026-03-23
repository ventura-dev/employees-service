package com.invex.employees.mappers;

import com.invex.employees.dto.EmployeeRequest;
import com.invex.employees.dto.EmployeeResponse;
import com.invex.employees.model.Employee;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.Period;

/**
 * The interface Employee mapper.
 */
@Mapper(componentModel = "spring")
public interface IEmployeeMapper {

    /**
     * To entity employee.
     *
     * @param employeeRequest the employee request
     * @return the employee
     */
    @Mapping(target = "age", expression = "java(calculateAge(employeeRequest.getBirthDate()))")
    Employee toEntity(EmployeeRequest employeeRequest);

    /**
     * To response employee response.
     *
     * @param employee the employee
     * @return the employee response
     */
    EmployeeResponse toResponse(Employee employee);


    /**
     * Update employee from request.
     *
     * @param employeeRequest the employee request
     * @param employee        the employee
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "age", expression = "java(calculateAge(employeeRequest.getBirthDate()))")
    void updateEmployeeFromRequest(EmployeeRequest employeeRequest, @MappingTarget Employee employee);



    default Integer calculateAge(LocalDate birthDate) {
        if (birthDate == null) {
            return null;
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}