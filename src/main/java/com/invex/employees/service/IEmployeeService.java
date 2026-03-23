package com.invex.employees.service;

import com.invex.employees.dto.EmployeeRequest;
import com.invex.employees.dto.EmployeeResponse;
import com.invex.employees.model.Employee;

import java.util.List;

/**
 * The interface Employee service.
 */
public interface IEmployeeService {

    /**
     * Gets all employees.
     *
     * @return the all employees
     */
    List<EmployeeResponse> getAllEmployees();

    /**
     * Gets employee by id.
     *
     * @param id the id
     * @return the employee by id
     */
    EmployeeResponse getEmployeeById(Long id);

    /**
     * Save employees list.
     *
     * @param employees the employees
     * @return the list
     */
    List<EmployeeResponse> saveEmployees(List<EmployeeRequest> employees);

    /**
     * Update employee employee.
     *
     * @param id       the id
     * @param employee the employee
     * @return the employee
     */
    EmployeeResponse updateEmployee(Long id, EmployeeRequest employee);

    /**
     * Delete employee.
     *
     * @param id the id
     */
    void deleteEmployee(Long id);

    /**
     * Search employees by name list.
     *
     * @param name the name
     * @return the list
     */
    List<EmployeeResponse> searchEmployeesByName(String name);
}
