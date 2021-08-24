package com.example.paraglider.controllers;

import com.example.paraglider.models.Brand;
import com.example.paraglider.repo.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@CrossOrigin
@RestController
public class BrandRestController {
    @Autowired
    BrandRepository brandRepository;

    @RequestMapping("/brandList")
    public List<Brand> getBrandsList(){
        List<Brand> brands = brandRepository.findAll();
        return brands;
    }
}
