package com.invex.employees.repository;

import com.invex.employees.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * The interface Employee repository.
 */
public interface EmployeeRepository extends JpaRepository<Employee,Long> {


    /**
     * Gets employee by first name.
     *
     * @param firstName the first name
     * @return the employee by first name
     */
    List<Employee> getEmployeeByFirstName(String firstName);


}
