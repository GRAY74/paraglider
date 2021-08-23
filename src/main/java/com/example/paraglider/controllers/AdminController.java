package com.example.paraglider.controllers;

import com.example.paraglider.models.Brand;
import com.example.paraglider.models.Paraglider;
import com.example.paraglider.repo.BrandRepository;
import com.example.paraglider.repo.ParagliderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.engine.ElementName;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    ParagliderRepository paragliderRepository;

    String brandName;
    Map<String, Boolean> brandSelect=new HashMap<>();
    List<Brand> brands = new ArrayList<>();
    List<Paraglider> paragliders = new ArrayList<>();

    @GetMapping("")
    public String admin(Model model,
                        @ModelAttribute("paraglider") Paraglider paraglider){
        brands = brandRepository.findAll();
        if(brandSelect.isEmpty()){
            brands.forEach(b->brandSelect.put(b.getName(), false));
        }

        if(brandName!=null){
            paragliders = paragliderRepository.findAllByBrandName(brandName);
        }

        model.addAttribute("paragliders", paragliders);
        model.addAttribute("brands", brands);
        model.addAttribute("brandSelect", brandSelect);
        return "admin";
    }


    @PostMapping("")
    public String addPagaglider(Model model,
                                @ModelAttribute("paraglider") @Valid Paraglider paraglider, BindingResult bindingResult,
                                @RequestParam(value="brandId", required = false)  Integer brandId ){


        if(brandId==null){
            bindingResult.rejectValue("brand", "error.paraglider","Выберите фирму");
        }

        if(bindingResult.hasErrors()){
            model.addAttribute("paragliders", paragliders);
            model.addAttribute("brands", brands);
            model.addAttribute("brandSelect", brandSelect);
            return "admin";
        }

        Brand brand = brandRepository.findById(brandId).get();
        paraglider.setBrand(brand);
        paragliderRepository.save(paraglider);
        return "redirect:/admin";
    }

    @PostMapping("/paraglider/{id}/remove")
    public String deliteParaglider(@PathVariable(value = "id") long id, Model model){
        Paraglider paraglider = paragliderRepository.findById(id).orElseThrow(() -> new IllegalStateException());
        paragliderRepository.delete(paraglider);
        return "redirect:/admin";
    }

    @GetMapping("/paraglider/{id}/edit")
    public String blogEdit(@PathVariable(value = "id") long id, Model model){
        if(!paragliderRepository.existsById(id)){
            return "redirect:/admin";
        }

        Iterable<Brand> brands = brandRepository.findAll();
        model.addAttribute("brands", brands);

        Optional<Paraglider> glider = paragliderRepository.findById(id);
        Paraglider paraglider = glider.get();
        model.addAttribute("paraglider", paraglider);
        return "paraglider-edit";
    }

    @PostMapping("/paraglider/{id}/edit")
    public String paragliderUpdate(Model model,
                                   @PathVariable(value = "id") long id,
                                   @RequestParam String name,
                                   @RequestParam int numberSections,
                                   @RequestParam float length,
                                   @RequestParam Integer brandId,
                                   @RequestParam Paraglider.Sertificat sertificat,
                                   @RequestParam (required = false)String description

    ){
        if(paragliderRepository.existsById(id)){
            Paraglider paraglider = paragliderRepository.findById(id).get();
            paraglider.setName(name);
            paraglider.setSertificat(sertificat);
            if(brandRepository.existsById(brandId)){
                Brand brand = brandRepository.findById(brandId).get();
                paraglider.setBrand(brand);
            }
            paraglider.setNumberSections(numberSections);
            paraglider.setLength(length);
            paraglider.setDescription(description);
            paragliderRepository.save(paraglider);
        }

        return "redirect:/admin";
    }

    @GetMapping("/brand/select")
    public String brandSelect(Model model,
                              @RequestParam(required = false) String brand){
        for(Map.Entry<String, Boolean>entry: brandSelect.entrySet()){
            entry.setValue(false);
        }
        brandSelect.put(brand, true);
        brandName = brand;
        return "redirect:/admin";
    }

}
