package com.example.company;

import com.example.employee.Employee;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/companies")
public class CompanyController {
    private List<Company> companies = new ArrayList<Company>();
    private int id = 0;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Company createCompanies(@RequestBody Company company) {
        Company newCampany = new Company(this.id++, company.name());
        companies.add(newCampany);
        return newCampany;
    }

    @GetMapping("/{id}")
    public Company getCompany(@PathVariable int id) {
        for (Company c : companies) {
            if (c.id().equals(id)){
                return c;
            }
        }
        return null;
    }

    @GetMapping()
    public List<Company> index(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        List<Company> resultEmployees = companies;
        if (!Objects.isNull(page) && !Objects.isNull(size)) {
            page = page <= 0 ? 1 : page;
            size = size < 0 ? 0 : size;
            resultEmployees = companies.stream().skip(size * (long) (page - 1)).limit(size).collect(Collectors.toList());
        }
        return resultEmployees;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Company updateCompany(@RequestBody Company company) {
        List<Company> companyList = companies.stream().filter(e -> !e.id().equals(company.id())).collect(Collectors.toList());
        companyList.add(company);
        companies = companyList;
        return company;
    }

    public void clear() {
        companies.clear();
        id = 0;
    }
}
