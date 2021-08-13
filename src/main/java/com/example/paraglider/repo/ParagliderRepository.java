package com.example.paraglider.repo;

import com.example.paraglider.models.Paraglider;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ParagliderRepository extends CrudRepository<Paraglider, Long> {

    public List<Paraglider> findAllByOrderByName();

    public List<Paraglider> findAllByBrandId(int id);

    public List<Paraglider> findAllByBrandName(String Name);

    public List<Paraglider> findAll();


}
