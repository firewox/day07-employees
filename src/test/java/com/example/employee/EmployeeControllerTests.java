package com.example.employee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeController employeeController;

    @BeforeEach
    void setUp() {
        employeeController.clear();
    }
    @Test
    void should_return_created_employee_when_post() throws Exception {
        //Given
        String requestBody = """
                {
                "id": 1,
                "name": "John Smith",
                "age": 32,
                "gender": "Male",
                "salary": 5000.0
                }
                """;
        MockHttpServletRequestBuilder request = post("/employees").contentType(MediaType.APPLICATION_JSON).content(requestBody);
        //When & Then
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.age").value(32))
                .andExpect(jsonPath("$.gender").value("Male"))
                .andExpect(jsonPath("$.salary").value(5000.0));
    }


    @Test
    void should_return_employee_when_get_employee_with_id_exists() throws Exception {
        //Given
        Employee employee = new Employee(null, "John Smith", 32, "Male", 5000.0);
        Employee newEmployee = employeeController.createEmployee(employee);
        MockHttpServletRequestBuilder request = get("/employees/" + newEmployee.id()).contentType(MediaType.APPLICATION_JSON);
        //When & Then
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(newEmployee.id()))
                .andExpect(jsonPath("$.name").value(newEmployee.name()))
                .andExpect(jsonPath("$.age").value(newEmployee.age()))
                .andExpect(jsonPath("$.gender").value(newEmployee.gender()))
                .andExpect(jsonPath("$.salary").value(newEmployee.salary()));
    }

    @Test
    void should_return_males_when_list_by_male() throws Exception {
        //Given
        Employee maleEmployee = employeeController.createEmployee(new Employee(null, "John Smith", 32, "Male", 5000.0));
        employeeController.createEmployee(new Employee(null, "Lily Smith", 20, "Female", 5000.0));
        MockHttpServletRequestBuilder request = get("/employees?gender=Male").contentType(MediaType.APPLICATION_JSON);
        //When & Then
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(maleEmployee.id()))
                .andExpect(jsonPath("$[0].name").value(maleEmployee.name()))
                .andExpect(jsonPath("$[0].age").value(maleEmployee.age()))
                .andExpect(jsonPath("$[0].gender").value(maleEmployee.gender()))
                .andExpect(jsonPath("$[0].salary").value(maleEmployee.salary()));
    }

    @Test
    void should_return_employees_when_get_all_employees() throws Exception {
        //Given
        employeeController.createEmployee(new Employee(null, "John Smith", 32, "Male", 5000.0));
        employeeController.createEmployee(new Employee(null, "Lily Smith", 20, "Female", 5000.0));
        MockHttpServletRequestBuilder request = get("/employees").contentType(MediaType.APPLICATION_JSON);
        //When & Then
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void should_return_updated_employees_when_update_an_employee_info() throws Exception {
        //Given
        Employee oldEmployee = employeeController.createEmployee(new Employee(null, "John Smith", 32, "Male", 5000.0));
        String requestBody = """
                {
                "id": %s,
                "name": "John Smith",
                "age": 35,
                "gender": "Male",
                "salary": 10000.0
                }
                """.formatted(oldEmployee.id());
        MockHttpServletRequestBuilder request = put("/employees/"+oldEmployee.id()).contentType(MediaType.APPLICATION_JSON).content(requestBody);
        //When & Then
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(oldEmployee.id()))
                .andExpect(jsonPath("$.age").value(35))
                .andExpect(jsonPath("$.salary").value(10000.0))
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.gender").value("Male"));
    }


}
