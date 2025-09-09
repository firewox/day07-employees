package com.example.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


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

    public void clear() {
        companies.clear();
        id = 0;
    }
}
