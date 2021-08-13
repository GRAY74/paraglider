package com.example.paraglider.controllers;

import com.example.paraglider.models.Brand;
import com.example.paraglider.models.Paraglider;
import com.example.paraglider.repo.BrandRepository;
import com.example.paraglider.repo.ParagliderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.engine.ElementName;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class AdminController {

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    ParagliderRepository paragliderRepository;

    String brandName;
    Map<String, Boolean> brandSelect=new HashMap<>();


    @GetMapping("/admin")
    public String admin(Model model){
        Iterable<Brand> brands = brandRepository.findAll();
        if(brandSelect.isEmpty()){
            brands.forEach(b->brandSelect.put(b.getName(), false));
        }

        model.addAttribute("brands", brands);
        model.addAttribute("brandSelect", brandSelect);

        List<Paraglider> paragliders = new ArrayList<>();
        if(brandName!=null){
            paragliders = paragliderRepository.findAllByBrandName(brandName);
        }
        model.addAttribute("paragliders", paragliders);
        return "admin";
    }


    @PostMapping("/paraglider/add")
    public String addPagaglider(Model model, HttpServletRequest request){
        String name = request.getParameter("name");

        Paraglider.Sertificat sertificat = Paraglider.Sertificat.valueOf(request.getParameter("sertificat"));


        if(!request.getParameter("brandId").isEmpty()) {
            Optional<Brand> brand = brandRepository.findById(Integer.parseInt(request.getParameter("brandId")));
            if (brand.isPresent()) {
                Brand br = brand.get();
                Paraglider paraglider = new Paraglider(br, name, sertificat);

                if(!request.getParameter("numberSections").isEmpty()){
                    paraglider.setNumberSections(Integer.parseInt(request.getParameter("numberSections")));
                }

                if(!request.getParameter("length").isEmpty()){
                    paraglider.setLength(Float.parseFloat(request.getParameter("length")));
                }

                paragliderRepository.save(paraglider);
            }
        }

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
                                   @RequestParam Paraglider.Sertificat sertificat

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
