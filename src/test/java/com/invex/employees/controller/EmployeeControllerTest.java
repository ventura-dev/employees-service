package com.invex.employees.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invex.employees.dto.EmployeeRequest;
import com.invex.employees.dto.EmployeeResponse;
import com.invex.employees.dto.EmployeeWrapperRequest;
import com.invex.employees.model.Sex;
import com.invex.employees.service.IEmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
@Import(GlobalExceptionHandler.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IEmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should return all employees")
    void shouldReturnAllEmployees() throws Exception {
        // Arrange
        EmployeeResponse employee = buildEmployeeResponse();

        when(employeeService.getAllEmployees()).thenReturn(List.of(employee));

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Jorge"))
                .andExpect(jsonPath("$[0].position").value("Backend Developer"));

        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    @DisplayName("Should return employee by id")
    void shouldReturnEmployeeById() throws Exception {
        EmployeeResponse employee = buildEmployeeResponse();

        when(employeeService.getEmployeeById(1L)).thenReturn(employee);

        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Jorge"))
                .andExpect(jsonPath("$.lastNamePaternal").value("Almazan"));

        verify(employeeService, times(1)).getEmployeeById(1L);
    }

    @Test
    @DisplayName("Should create employees")
    void shouldCreateEmployees() throws Exception {
        EmployeeRequest employeeRequest = buildEmployeeRequest();
        EmployeeWrapperRequest wrapperRequest = new EmployeeWrapperRequest();
        wrapperRequest.setEmployees(List.of(employeeRequest));

        EmployeeResponse employeeResponse = buildEmployeeResponse();

        when(employeeService.saveEmployees(any())).thenReturn(List.of(employeeResponse));

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrapperRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Jorge"))
                .andExpect(jsonPath("$[0].position").value("Backend Developer"));

        verify(employeeService, times(1)).saveEmployees(any());
    }

    @Test
    @DisplayName("Should return bad request when employee request is invalid")
    void shouldReturnBadRequestWhenEmployeeRequestIsInvalid() throws Exception {
        String invalidJson = """
                {
                  "employees": [
                    {
                      "firstName": "JorgeJorgeJorgeJorgeJorgeJorgeJorgeJorgeJorgeJorge",
                      "secondName": "Luis",
                      "lastNamePaternal": "Almazan",
                      "lastNameMaternal": "Hernandez",
                      "age": 30,
                      "sex": "M",
                      "birthDate": "1994-05-10",
                      "position": "Backend Developer",
                      "active": true
                    }
                  ]
                }
                """;

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors['employees[0].firstName']")
                        .value("First name must not exceed 50 characters"));

        verifyNoInteractions(employeeService);
    }

    @Test
    @DisplayName("Should update employee")
    void shouldUpdateEmployee() throws Exception {
        EmployeeRequest employeeRequest = buildEmployeeRequest();
        EmployeeResponse employeeResponse = buildEmployeeResponse();

        when(employeeService.updateEmployee(eq(1L), any(EmployeeRequest.class))).thenReturn(employeeResponse);

        mockMvc.perform(put("/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Jorge"))
                .andExpect(jsonPath("$.position").value("Backend Developer"));

        verify(employeeService, times(1)).updateEmployee(eq(1L), any(EmployeeRequest.class));
    }

    @Test
    @DisplayName("Should delete employee")
    void shouldDeleteEmployee() throws Exception {
        mockMvc.perform(delete("/employees/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(employeeService, times(1)).deleteEmployee(1L);
    }

    @Test
    @DisplayName("Should search employees by name")
    void shouldSearchEmployeesByName() throws Exception {
        EmployeeResponse employee = buildEmployeeResponse();

        when(employeeService.searchEmployeesByName("jor")).thenReturn(List.of(employee));

        mockMvc.perform(get("/employees/search")
                        .param("name", "jor"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Jorge"));

        verify(employeeService, times(1)).searchEmployeesByName("jor");
    }

    private EmployeeRequest buildEmployeeRequest() {
        EmployeeRequest request = new EmployeeRequest();
        request.setFirstName("Jorge");
        request.setSecondName("Luis");
        request.setLastNamePaternal("Almazan");
        request.setLastNameMaternal("Hernandez");
        request.setSex(Sex.M);
        request.setBirthDate(LocalDate.of(1994, 5, 10));
        request.setPosition("Backend Developer");
        request.setActive(true);
        return request;
    }

    private EmployeeResponse buildEmployeeResponse() {
        EmployeeResponse response = new EmployeeResponse();
        response.setId(1L);
        response.setFirstName("Jorge");
        response.setSecondName("Luis");
        response.setLastNamePaternal("Almazan");
        response.setLastNameMaternal("Hernandez");
        response.setAge(30);
        response.setSex(Sex.M);
        response.setBirthDate(LocalDate.of(1994, 5, 10));
        response.setPosition("Backend Developer");
        response.setCreatedAt(LocalDateTime.of(2026, 3, 21, 12, 0));
        response.setActive(true);
        return response;
    }
}