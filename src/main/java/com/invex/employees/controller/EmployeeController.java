package com.invex.employees.controller;

import com.invex.employees.dto.EmployeeRequest;
import com.invex.employees.dto.EmployeeResponse;
import com.invex.employees.dto.EmployeeWrapperRequest;
import com.invex.employees.service.IEmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * The type Employee controller.
 */
@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@Tag(name = "Employees", description = "API for employee management")
@Slf4j
public class EmployeeController {

    private final IEmployeeService employeeService;

    /**
     * GET /employees
     * Returns the list of all registered employees.
     *
     * @return the all employees
     */
    @Operation(
            summary = "Get all employees",
            description = "Returns the complete list of employees registered in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee list retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    /**
     * GET /employees/{id}
     * Retrieves the details of an employee by ID.
     *
     * @param id the id
     * @return the employee by id
     */
    @Operation(
            summary = "Get employee by ID",
            description = "Retrieves the details of a specific employee by its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee found"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(
            @Parameter(description = "Unique employee ID", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    /**
     * POST /employees
     * Allows inserting one or more employees in a single request.
     *
     * @param employeWrapper the employe wrapper
     * @return the response entity
     */
    @Operation(
            summary = "Create employees",
            description = "Allows registering one or more employees in a single request"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employees created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping
    public ResponseEntity<List<EmployeeResponse>> saveEmployees(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "List of employees to be created",
                    required = true
            )
            @RequestBody @Valid EmployeeWrapperRequest employeWrapper) {
        log.info("Creating {} Employee(s)", employeWrapper.getEmployees().size());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeeService.saveEmployees(employeWrapper.getEmployees()));
    }

    /**
     * PUT /employees/{id}
     * Updates all or some fields of an employee.
     *
     * @param id       the id
     * @param employee the employee
     * @return the response entity
     */
    @Operation(
            summary = "Update employee",
            description = "Updates all or some fields of an existing employee"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee updated successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @Parameter(description = "Unique employee ID to update", example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Employee data to update (partial fields are allowed)",
                    required = true
            )
            @RequestBody EmployeeRequest employee) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employee));
    }

    /**
     * DELETE /employees/{id}
     * Deletes an employee by ID.
     *
     * @param id the id
     * @return the response entity
     */
    @Operation(
            summary = "Delete employee",
            description = "Deletes an employee from the system by its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Employee deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @Parameter(description = "Unique employee ID to delete", example = "1")
            @PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /employees/search?name={name}
     * Search employees by name (partial match).
     *
     * @param name the name
     * @return the response entity
     */
    @Operation(
            summary = "Search employees by name",
            description = "Performs a partial search for employees by first name, second name, or last names"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid search parameter")
    })
    @GetMapping("/search")
    public ResponseEntity<List<EmployeeResponse>> searchEmployeesByName(
            @Parameter(
                    description = "Text to search in employee first name or last names",
                    example = "jor"
            )
            @RequestParam String name) {
        return ResponseEntity.ok(employeeService.searchEmployeesByName(name));
    }
}