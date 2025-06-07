package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.model.Pharmacist;
import com.example.repository.PharmacistRepository;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/pharmacists")
public class PharmacistController {

    @Autowired
    private PharmacistRepository pharmacistRepository;

    @GetMapping
    public String listPharmacists(@RequestParam(required = false, name = "search") String search, Model model) {
        List<Pharmacist> pharmacists;
        
        if (search != null && !search.trim().isEmpty()) {
            pharmacists = pharmacistRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(search, search);
        } else {
            pharmacists = pharmacistRepository.findAll();
        }

        model.addAttribute("pharmacists", pharmacists);
        return "pharmacists/list";
    }

    @GetMapping("/new")
    public String showPharmacistForm(Model model) {
        model.addAttribute("pharmacist", new Pharmacist());
        return "pharmacists/form";
    }

    @PostMapping
    public String createPharmacist(@ModelAttribute Pharmacist pharmacist) {
        pharmacist.setJoiningDate(LocalDate.now());
        pharmacist.setActive(true);
        pharmacistRepository.save(pharmacist);
        return "redirect:/pharmacists";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable(name = "id") Long id, Model model) {
        Pharmacist pharmacist = pharmacistRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid pharmacist ID"));
        
        model.addAttribute("pharmacist", pharmacist);
        return "pharmacists/form";
    }

    @PostMapping("/{id}")
    public String updatePharmacist(@PathVariable(name = "id") Long id, @ModelAttribute Pharmacist pharmacist) {
        pharmacist.setId(id);
        pharmacistRepository.save(pharmacist);
        return "redirect:/pharmacists";
    }

    @GetMapping("/{id}/delete")
    public String deletePharmacist(@PathVariable(name = "id") Long id) {
        Pharmacist pharmacist = pharmacistRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid pharmacist ID"));
        pharmacist.setActive(false);
        pharmacistRepository.save(pharmacist);
        return "redirect:/pharmacists";
    }
} 