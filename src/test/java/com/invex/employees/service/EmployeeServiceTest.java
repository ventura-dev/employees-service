package com.invex.employees.service;

import com.invex.employees.dto.EmployeeRequest;
import com.invex.employees.dto.EmployeeResponse;
import com.invex.employees.mappers.IEmployeeMapper;
import com.invex.employees.model.Employee;
import com.invex.employees.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for EmployeeService.
 */
@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private IEmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void shouldReturnAllEmployees() {
        // Arrange
        Employee employee1 = new Employee();
        employee1.setId(1L);
        employee1.setFirstName("Jose");

        Employee employee2 = new Employee();
        employee2.setId(2L);
        employee2.setFirstName("Ana");

        EmployeeResponse response1 = new EmployeeResponse();
        response1.setId(1L);
        response1.setFirstName("Jose");

        EmployeeResponse response2 = new EmployeeResponse();
        response2.setId(2L);
        response2.setFirstName("Ana");

        when(employeeRepository.findAll()).thenReturn(List.of(employee1, employee2));
        when(employeeMapper.toResponse(employee1)).thenReturn(response1);
        when(employeeMapper.toResponse(employee2)).thenReturn(response2);

        List<EmployeeResponse> result = employeeService.getAllEmployees();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Jose", result.get(0).getFirstName());
        assertEquals("Ana", result.get(1).getFirstName());

        verify(employeeRepository).findAll();
        verify(employeeMapper, times(2)).toResponse(any(Employee.class));
    }

    @Test
    void shouldReturnEmployeeById() {
        Long id = 1L;

        Employee employee = new Employee();
        employee.setId(id);
        employee.setFirstName("Jose");

        EmployeeResponse response = new EmployeeResponse();
        response.setId(id);
        response.setFirstName("Jose");

        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));
        when(employeeMapper.toResponse(employee)).thenReturn(response);

        EmployeeResponse result = employeeService.getEmployeeById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Jose", result.getFirstName());

        verify(employeeRepository).findById(id);
        verify(employeeMapper).toResponse(employee);
    }

    @Test
    void shouldThrowExceptionWhenEmployeeByIdNotFound() {
        Long id = 99L;

        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> employeeService.getEmployeeById(id));

        assertEquals("Employee not found with id: 99", exception.getMessage());

        verify(employeeRepository).findById(id);
        verify(employeeMapper, never()).toResponse(any(Employee.class));
    }

    @Test
    void shouldSaveEmployees() {
        EmployeeRequest request1 = new EmployeeRequest();
        request1.setFirstName("Jose");

        EmployeeRequest request2 = new EmployeeRequest();
        request2.setFirstName("Ana");

        Employee entity1 = new Employee();
        entity1.setFirstName("Jose");

        Employee entity2 = new Employee();
        entity2.setFirstName("Ana");

        Employee saved1 = new Employee();
        saved1.setId(1L);
        saved1.setFirstName("Jose");

        Employee saved2 = new Employee();
        saved2.setId(2L);
        saved2.setFirstName("Ana");

        EmployeeResponse response1 = new EmployeeResponse();
        response1.setId(1L);
        response1.setFirstName("Jose");

        EmployeeResponse response2 = new EmployeeResponse();
        response2.setId(2L);
        response2.setFirstName("Ana");

        when(employeeMapper.toEntity(request1)).thenReturn(entity1);
        when(employeeMapper.toEntity(request2)).thenReturn(entity2);
        when(employeeRepository.saveAll(List.of(entity1, entity2))).thenReturn(List.of(saved1, saved2));
        when(employeeMapper.toResponse(saved1)).thenReturn(response1);
        when(employeeMapper.toResponse(saved2)).thenReturn(response2);

        List<EmployeeResponse> result = employeeService.saveEmployees(List.of(request1, request2));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());

        verify(employeeMapper).toEntity(request1);
        verify(employeeMapper).toEntity(request2);
        verify(employeeRepository).saveAll(List.of(entity1, entity2));
        verify(employeeMapper).toResponse(saved1);
        verify(employeeMapper).toResponse(saved2);
    }

    @Test
    void shouldUpdateEmployee() {
        Long id = 1L;

        EmployeeRequest request = new EmployeeRequest();
        request.setFirstName("Updated Jose");

        Employee existingEmployee = new Employee();
        existingEmployee.setId(id);
        existingEmployee.setFirstName("Jose");

        Employee updatedEmployee = new Employee();
        updatedEmployee.setId(id);
        updatedEmployee.setFirstName("Updated Jose");

        EmployeeResponse response = new EmployeeResponse();
        response.setId(id);
        response.setFirstName("Updated Jose");

        when(employeeRepository.findById(id)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(existingEmployee)).thenReturn(updatedEmployee);
        when(employeeMapper.toResponse(updatedEmployee)).thenReturn(response);

        EmployeeResponse result = employeeService.updateEmployee(id, request);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Updated Jose", result.getFirstName());

        verify(employeeRepository).findById(id);
        verify(employeeMapper).updateEmployeeFromRequest(request, existingEmployee);
        verify(employeeRepository).save(existingEmployee);
        verify(employeeMapper).toResponse(updatedEmployee);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingEmployee() {
        Long id = 99L;
        EmployeeRequest request = new EmployeeRequest();

        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> employeeService.updateEmployee(id, request));

        assertEquals("Employee not found with id: 99", exception.getMessage());

        verify(employeeRepository).findById(id);
        verify(employeeMapper, never()).updateEmployeeFromRequest(any(), any());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void shouldDeleteEmployee() {
        Long id = 1L;

        Employee employee = new Employee();
        employee.setId(id);
        employee.setFirstName("Jose");

        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));

        employeeService.deleteEmployee(id);

        verify(employeeRepository).findById(id);
        verify(employeeRepository).delete(employee);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingEmployee() {
        Long id = 99L;

        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> employeeService.deleteEmployee(id));

        assertEquals("Employee not found with id: 99", exception.getMessage());

        verify(employeeRepository).findById(id);
        verify(employeeRepository, never()).delete(any(Employee.class));
    }

    @Test
    void shouldSearchEmployeesByName() {
        String name = "Jose";

        Employee employee1 = new Employee();
        employee1.setId(1L);
        employee1.setFirstName("Jose");

        Employee employee2 = new Employee();
        employee2.setId(2L);
        employee2.setFirstName("Jordi");

        EmployeeResponse response1 = new EmployeeResponse();
        response1.setId(1L);
        response1.setFirstName("Jose");

        EmployeeResponse response2 = new EmployeeResponse();
        response2.setId(2L);
        response2.setFirstName("Jordi");

        when(employeeRepository.getEmployeeByFirstName(name)).thenReturn(List.of(employee1, employee2));
        when(employeeMapper.toResponse(employee1)).thenReturn(response1);
        when(employeeMapper.toResponse(employee2)).thenReturn(response2);

        List<EmployeeResponse> result = employeeService.searchEmployeesByName(name);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Jose", result.get(0).getFirstName());
        assertEquals("Jordi", result.get(1).getFirstName());

        verify(employeeRepository).getEmployeeByFirstName(name);
        verify(employeeMapper, times(2)).toResponse(any(Employee.class));
    }
}