package com.example.employee;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    private List<Employee> employees = new ArrayList<Employee>();
    private int id = 0;

    public Employee createEmployee(Employee employee) {
        Employee newEmployee = new Employee(this.id++, employee.name(), employee.age(), employee.gender(), employee.salary());
        employees.add(newEmployee);
        return newEmployee;
    }
}
