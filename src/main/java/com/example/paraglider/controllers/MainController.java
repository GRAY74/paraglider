package com.example.paraglider.controllers;

import com.example.paraglider.models.Brand;
import com.example.paraglider.models.Paraglider;
import com.example.paraglider.repo.BrandRepository;
import com.example.paraglider.repo.ParagliderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class MainController {

    List<Paraglider> paragliderList;
    String[] brandsArray;
    List<Paraglider.Sertificat> sertificatList;
    Map<String, Boolean> brandSelect = new TreeMap<>();
    Map<Paraglider.Sertificat, Boolean> sertificatSelect = new TreeMap<>();

    boolean brandSort = false;
    boolean sertificatSort = false;

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    ParagliderRepository paragliderRepository;

    @GetMapping("/login")
    public String login() {
        return "/login";
    }

    @GetMapping("/")
    public String getParagliders(Model model){

        if(brandSelect.isEmpty()){
            List<Brand> brands = brandRepository.findAllByOrderByName();
            brands.forEach(b->brandSelect.put(b.getName(), false));
        }

        if(sertificatSelect.isEmpty()){
            for(Paraglider.Sertificat sertificat: Paraglider.Sertificat.values()){
                sertificatSelect.put(sertificat, false);
            }
        }

        if(brandsArray!=null){
            paragliderList = getParaglidersByBransArray(brandsArray);
        } else {
            paragliderList = paragliderRepository.findAll();
        }

        if(sertificatList!=null){
            paragliderList = paragliderList.stream().filter(p -> sertificatList.contains(p.getSertificat())).collect(Collectors.toList());
        }

        if(brandSort){
            Collections.sort(paragliderList, ((p1, p2)->p1.getBrand().getName().compareTo(p2.getBrand().getName())));
        }
        if(sertificatSort){
            Collections.sort(paragliderList, ((p1, p2)->p1.getSertificat().compareTo(p2.getSertificat())));
        }

        model.addAttribute("brandSelect", brandSelect);
        model.addAttribute("sertificatSelet", sertificatSelect);
        model.addAttribute("paragliders", paragliderList);
        return "main";
    }

    @PostMapping("/paragliders/filter")
    public String filter(Model model,
                         @RequestParam(required = false) String[] brand,
                         @RequestParam(required = false) Paraglider.Sertificat[] sertificat
    ){
        brandsArray = brand;

        for(Map.Entry<String, Boolean> select: brandSelect.entrySet()){
            select.setValue(false);
        }
        if(brand != null){
            for(String br: brand){
                brandSelect.put(br, true);
            }
        }

        for(Map.Entry<Paraglider.Sertificat, Boolean> select: sertificatSelect.entrySet()){
            select.setValue(false);
        }

        if(sertificat != null){
            sertificatList = new ArrayList<>(Arrays.asList(sertificat));
            for(Paraglider.Sertificat sert: sertificat){
                sertificatSelect.put(sert, true);
            }
        } else {
            sertificatList = null;
        }
        return "redirect:/";
    }

    @GetMapping("/brandSort")
    public String SortByBrand(){
        brandSort = true;
        sertificatSort = false;
        return "redirect:/";
    }

    @GetMapping("/sertificatSort")
    public String SortBySertificat(){
        brandSort = false;
        sertificatSort = true;
        return "redirect:/";
    }


    public List<Paraglider> getParaglidersByBransArray(String[] brandsArray){
        List<Paraglider> gliders = new ArrayList<>();
        for(String brand:brandsArray){
            gliders.addAll(paragliderRepository.findAllByBrandName(brand));
        }
        return gliders;
    }

    @GetMapping("/brands")
    public String brands(Model model, @ModelAttribute("brand")Brand brand){
        List<Brand> brandList=brandRepository.findAll();
        Collections.sort(brandList, (b1, b2)->(b1.getName().compareTo(b2.getName())));
        model.addAttribute("brands", brandList);
        return "brands";
    }
}
