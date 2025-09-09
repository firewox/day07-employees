package com.example.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    private List<Employee> employees = new ArrayList<Employee>();
    private int id = 0;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@RequestBody Employee employee) {
        Employee newEmployee = new Employee(this.id++, employee.name(), employee.age(), employee.gender(), employee.salary());
        employees.add(newEmployee);
        Employee newEmployee1 = employeeService.createEmployee(employee);
        return newEmployee;
    }

    @GetMapping("/{id}")
    public Employee getEmployee(@PathVariable int id) {
        for (Employee e : employees) {
            if (e.id().equals(id)){
                return e;
            }
        }
        return null;
    }

    @GetMapping()
    public List<Employee> index(@RequestParam(required = false) String gender) {
        ArrayList<Employee> indexEmployees = new ArrayList<>();
        for (Employee e : employees) {
            if (e.gender().equalsIgnoreCase(gender)){
                indexEmployees.add(e);
            }
        }
        return indexEmployees;
    }

    public void clear() {
        employees.clear();
    }
}
