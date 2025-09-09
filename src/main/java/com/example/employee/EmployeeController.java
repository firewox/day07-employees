package com.example.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


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

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Employee updateEmployee(@RequestBody Employee employee) {
        List<Employee> employeeList = employees.stream().filter(e -> !e.id().equals(employee.id())).collect(Collectors.toList());
        employeeList.add(employee);
        return employee;
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
    public List<Employee> index(@RequestParam(required = false) String gender, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        List<Employee> resultEmployees = employees;
        if (!Objects.isNull(page) && !Objects.isNull(size)) {
            page = page <= 0 ? 1 : page;
            size = size < 0 ? 0 : size;
            resultEmployees = employees.stream().skip(size * (long) (page - 1)).limit(size).collect(Collectors.toList());
        }
        if (gender != null) {
            resultEmployees = resultEmployees.stream().filter(e -> e.gender().equalsIgnoreCase(gender)).collect(Collectors.toList());
        }
        return resultEmployees;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        employees = employees.stream().filter(e -> !e.id().equals(id)).collect(Collectors.toList());
    }

    public void clear() {
        employees.clear();
    }
}
