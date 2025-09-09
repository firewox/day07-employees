package com.example.employee;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeController employeeController;

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

}
