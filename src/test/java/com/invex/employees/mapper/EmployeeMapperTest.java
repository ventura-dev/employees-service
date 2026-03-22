package com.invex.employees.mapper;


import com.invex.employees.dto.EmployeeRequest;
import com.invex.employees.dto.EmployeeResponse;
import com.invex.employees.mappers.IEmployeeMapper;
import com.invex.employees.model.Employee;
import com.invex.employees.model.Sex;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmployeeMapperTest {

    private final IEmployeeMapper employeeMapper = Mappers.getMapper(IEmployeeMapper.class);

    @Test
    void shouldMapEmployeeRequestToEmployee() {
        EmployeeRequest request = new EmployeeRequest();
        request.setFirstName("Jorge");
        request.setSecondName("Luis");
        request.setLastNamePaternal("Almazan");
        request.setLastNameMaternal("Hernandez");
        request.setAge(30);
        request.setSex(Sex.M);
        request.setBirthDate(LocalDate.of(1995, 1, 1));
        request.setPosition("Backend Developer");
        request.setActive(true);

        Employee result = employeeMapper.toEntity(request);

        assertNotNull(result);
        assertEquals("Jorge", result.getFirstName());
        assertEquals("Luis", result.getSecondName());
        assertEquals("Almazan", result.getLastNamePaternal());
        assertEquals("Hernandez", result.getLastNameMaternal());
        assertEquals(30, result.getAge());
        assertEquals(Sex.M, result.getSex());
        assertEquals(LocalDate.of(1995, 1, 1), result.getBirthDate());
        assertEquals("Backend Developer", result.getPosition());
        assertTrue(result.getActive());
    }

    @Test
    void shouldMapEmployeeToEmployeeResponse() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Jorge");
        employee.setSecondName("Luis");
        employee.setLastNamePaternal("Almazan");
        employee.setLastNameMaternal("Hernandez");
        employee.setAge(30);
        employee.setSex(Sex.M);
        employee.setBirthDate(LocalDate.of(1995, 1, 1));
        employee.setPosition("Backend Developer");
        employee.setCreatedAt(LocalDateTime.of(2026, 3, 21, 12, 0));
        employee.setActive(true);

        EmployeeResponse result = employeeMapper.toResponse(employee);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Jorge", result.getFirstName());
        assertEquals("Luis", result.getSecondName());
        assertEquals("Almazan", result.getLastNamePaternal());
        assertEquals("Hernandez", result.getLastNameMaternal());
        assertEquals(30, result.getAge());
        assertEquals(Sex.M, result.getSex());
        assertEquals(LocalDate.of(1995, 1, 1), result.getBirthDate());
        assertEquals("Backend Developer", result.getPosition());
        assertEquals(LocalDateTime.of(2026, 3, 21, 12, 0), result.getCreatedAt());
        assertTrue(result.getActive());
    }

    @Test
    void shouldUpdateEmployeeFromRequestIgnoringNullValues() {
        EmployeeRequest request = new EmployeeRequest();
        request.setFirstName("Updated Jorge");
        request.setPosition("Senior Backend Developer");
        request.setAge(null);

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Jorge");
        employee.setSecondName("Luis");
        employee.setLastNamePaternal("Almazan");
        employee.setLastNameMaternal("Hernandez");
        employee.setAge(30);
        employee.setSex(Sex.M);
        employee.setBirthDate(LocalDate.of(1995, 1, 1));
        employee.setPosition("Backend Developer");
        employee.setCreatedAt(LocalDateTime.of(2026, 3, 21, 12, 0));
        employee.setActive(true);

        employeeMapper.updateEmployeeFromRequest(request, employee);

        assertEquals(1L, employee.getId());
        assertEquals("Updated Jorge", employee.getFirstName());
        assertEquals("Luis", employee.getSecondName());
        assertEquals("Almazan", employee.getLastNamePaternal());
        assertEquals(30, employee.getAge());
        assertEquals("Senior Backend Developer", employee.getPosition());
        assertEquals(LocalDateTime.of(2026, 3, 21, 12, 0), employee.getCreatedAt());
    }

    @Test
    void shouldUpdateAllFieldsFromRequest() {

        EmployeeRequest request = new EmployeeRequest();
        request.setFirstName("Jorge");
        request.setSecondName("Luis");
        request.setLastNamePaternal("Almazan");
        request.setLastNameMaternal("Hernandez");
        request.setAge(30);
        request.setSex(Sex.M);
        request.setBirthDate(LocalDate.of(1994, 5, 10));
        request.setPosition("Backend Developer");
        request.setActive(true);

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("OldName");
        employee.setSecondName("OldSecond");
        employee.setLastNamePaternal("OldPaternal");
        employee.setLastNameMaternal("OldMaternal");
        employee.setAge(20);
        employee.setSex(Sex.F);
        employee.setBirthDate(LocalDate.of(2000, 1, 1));
        employee.setPosition("Old Position");
        employee.setCreatedAt(LocalDateTime.of(2026, 3, 21, 12, 0));
        employee.setActive(false);

        employeeMapper.updateEmployeeFromRequest(request, employee);

        assertEquals(1L, employee.getId());
        assertEquals("Jorge", employee.getFirstName());
        assertEquals("Luis", employee.getSecondName());
        assertEquals("Almazan", employee.getLastNamePaternal());
        assertEquals("Hernandez", employee.getLastNameMaternal());
        assertEquals(30, employee.getAge());
        assertEquals(Sex.M, employee.getSex());
        assertEquals(LocalDate.of(1994, 5, 10), employee.getBirthDate());
        assertEquals("Backend Developer", employee.getPosition());
        assertTrue(employee.getActive());
        assertEquals(LocalDateTime.of(2026, 3, 21, 12, 0), employee.getCreatedAt());
    }


    @Test
    void shouldUpdateNullFromRequest() {

        Employee employee = new Employee();
        employee.setId(1L);
        employeeMapper.updateEmployeeFromRequest(null, employee);
        employeeMapper.toResponse(null);
        employeeMapper.toEntity(null);

        assertEquals(1L, employee.getId());

    }
}
