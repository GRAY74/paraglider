package com.example.paraglider.controllers;


import com.example.paraglider.models.Brand;
import com.example.paraglider.repo.BrandRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/brand")
public class BrandsController {

    @Autowired
    BrandRepository brandRepository;

    List<Brand> brandList = new ArrayList<>();

    @GetMapping("/add")
    public String brands(Model model, @ModelAttribute("brand")Brand brand){
        brandList=brandRepository.findAll();
        Collections.sort(brandList, (b1, b2)->(b1.getName().compareTo(b2.getName())));
        model.addAttribute("brands", brandList);
        return "brand-add";
    }


    @PostMapping ("/add")
    public String brandAdd(Model model, @ModelAttribute("brand") @Valid Brand brand, BindingResult bindingResult){

        if(bindingResult.hasErrors()) {
            model.addAttribute("brands", brandList);
            return "brand-add";
        }
        brandRepository.save(brand);
        return "redirect:/brand/add";
    }


    @PostMapping("/{id}/remove")
    public String drandRemove(Model model,
                              @PathVariable(value = "id") int id){
        Brand brand = brandRepository.findById(id).orElse(null);
        if(brand!=null){
            try{
                brandRepository.delete(brand);
            } catch (Exception e){
            }
        }
        return "redirect:/brand/add";
    }

    @GetMapping("/{id}/edit")
    public String brandEdit(Model model,
                            @PathVariable(value = "id") int id){
        if(brandRepository.existsById(id)){
            Brand brand = brandRepository.findById(id).get();
            model.addAttribute("brand", brand);
            return "brand-edit";
        }
        return "brand-add";
    }

    @PostMapping("/{id}/edit")
    public String brandUpdate(Model model,
                              @PathVariable(value = "id") int id,
                              @ModelAttribute("brand") @Valid Brand brand, BindingResult bindingResult ){
        if(bindingResult.hasErrors()){
            return "brand-edit";
        }
        brandRepository.save(brand);
        return "redirect:/brand/add";
    }

}
