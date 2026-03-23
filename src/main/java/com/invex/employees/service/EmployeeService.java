package com.invex.employees.service;

import com.invex.employees.dto.EmployeeRequest;
import com.invex.employees.dto.EmployeeResponse;
import com.invex.employees.mappers.IEmployeeMapper;
import com.invex.employees.model.Employee;
import com.invex.employees.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Employee service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService implements IEmployeeService {

    private final EmployeeRepository employeeRepository;
    private final IEmployeeMapper employeeMapper;

    @Override
    public List<EmployeeResponse> getAllEmployees() {
        log.info("GetAll Employees");
        return employeeRepository.findAll()
                .stream()
                .map(employeeMapper::toResponse)
                .toList();
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id) {
        log.info("Call getEmployeeById");

        return employeeRepository.findById(id)
                .map(employeeMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    }

    @Override
    public List<EmployeeResponse> saveEmployees(List<EmployeeRequest> employees) {
        log.info("Call saveEmployees");

        return employeeRepository.saveAll(
                        employees.stream()
                                .map(employeeMapper::toEntity)
                                .toList()
                ).stream()
                .map(employeeMapper::toResponse)
                .toList();
    }

    @Override
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest employeeRequest) {
        log.info("Call updateEmployee");
        return employeeRepository.findById(id)
                .map(existingEmployee -> {
                    log.debug("Existign Employee "+existingEmployee.getFirstName());
                    employeeMapper.updateEmployeeFromRequest(employeeRequest, existingEmployee);
                    return employeeRepository.save(existingEmployee);
                })
                .map(employeeMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    }

    @Override
    public void deleteEmployee(Long id) {
        log.info("Call deleteEmployee");
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

        employeeRepository.delete(existingEmployee);
    }

    @Override
    public List<EmployeeResponse> searchEmployeesByName(String name) {
        log.info("Call searchEmployeesByName");
        return employeeRepository.getEmployeeByFirstName(name)
                .stream()
                .map(employeeMapper::toResponse)
                .toList();
    }
}