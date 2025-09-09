package com.example.company;

import com.example.employee.Employee;
import com.example.employee.EmployeeController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CompanyControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CompanyController companyController;

    @BeforeEach
    void setUp() {
        companyController.clear();
    }
    @Test
    void should_return_created_company_when_post() throws Exception {
        //Given
        String requestBody = """
                {
                "name": "Alibaba"
                }
                """;
        MockHttpServletRequestBuilder request = post("/companies").contentType(MediaType.APPLICATION_JSON).content(requestBody);
        //When & Then
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Alibaba"));
    }

    @Test
    void should_return_company_when_get_company_with_id_exists() throws Exception {
        //Given
        Company company = new Company(null, "Alibaba");
        Company newCompany = companyController.createCompanies(company);
        MockHttpServletRequestBuilder request = get("/companies/" + newCompany.id()).contentType(MediaType.APPLICATION_JSON);
        //When & Then
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(newCompany.id()))
                .andExpect(jsonPath("$.name").value(newCompany.name()));
    }

    @Test
    void should_return_paged_companies_when_page_query_with_page_size_and_page_number() throws Exception {
        //Given
        companyController.createCompanies(new Company(null, "Alibaba"));
        companyController.createCompanies(new Company(null, "ByteDance"));
        companyController.createCompanies(new Company(null, "Huawei"));
        companyController.createCompanies(new Company(null, "Tencent"));
        companyController.createCompanies(new Company(null, "Zhipu"));
        companyController.createCompanies(new Company(null, "DeepSeek"));
        companyController.createCompanies(new Company(null, "RedNote"));
        companyController.createCompanies(new Company(null, "DJI"));
        int page_number = 1;
        int page_size = 5;
        MockHttpServletRequestBuilder request = get("/companies?" + "page=" + page_number + "&size=" + page_size).contentType(MediaType.APPLICATION_JSON);
        //When & Then
        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    void should_return_companies_when_get_all_companies() throws Exception {
        //Given
        companyController.createCompanies(new Company(null, "Alibaba"));
        companyController.createCompanies(new Company(null, "ByteDance"));
        companyController.createCompanies(new Company(null, "Huawei"));
        companyController.createCompanies(new Company(null, "Tencent"));
        companyController.createCompanies(new Company(null, "Zhipu"));
        companyController.createCompanies(new Company(null, "DeepSeek"));
        companyController.createCompanies(new Company(null, "RedNote"));
        companyController.createCompanies(new Company(null, "DJI"));
        MockHttpServletRequestBuilder request = get("/companies").contentType(MediaType.APPLICATION_JSON);
        //When & Then
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(8));
    }

    @Test
    void should_return_updated_companies_when_update_an_company_info() throws Exception {
        //Given
        Company oldCompany = companyController.createCompanies(new Company(null, "Alibaba"));
        String requestBody = """
                {
                "id": %d,
                "name": "ByteDance"
                }
                """.formatted(oldCompany.id());
        MockHttpServletRequestBuilder request = put("/companies/"+oldCompany.id()).contentType(MediaType.APPLICATION_JSON).content(requestBody);
        //When & Then
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(oldCompany.id()))
                .andExpect(jsonPath("$.name").value("ByteDance"));
    }
}
