package com.example.paraglider.repo;

import com.example.paraglider.models.Brand;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BrandRepository extends CrudRepository<Brand, Integer> {

    List<Brand>findAllByOrderByName();
    List<Brand>findAll();
}
