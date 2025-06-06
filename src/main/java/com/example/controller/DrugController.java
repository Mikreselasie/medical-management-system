package com.example.controller;

// Importing required dependencies
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.model.Drug;
import com.example.model.Diseases;
import com.example.repository.DrugRepository;
import com.example.repository.DiseasesRepository;

import java.util.List;

// Marks this class as a Spring MVC controller
@Controller
@RequestMapping("/drugs") // Base path for all endpoints in this controller
public class DrugController {

    // Injecting the DrugRepository (interface to the database for Drug)
    @Autowired
    private DrugRepository drugRepository;

    // Injecting the DiseasesRepository (interface to the database for Diseases)
    @Autowired
    private DiseasesRepository diseasesRepository;

    // ================================
    // 1. LIST ALL DRUGS + SEARCH
    // ================================
    @GetMapping
    public String listDrugs(@RequestParam(required = false, name = "search") String search, Model model) {
        List<Drug> drugs;
        
        // If a search query is given, filter drugs by name or manufacturer
        if (search != null && !search.trim().isEmpty()) {
            drugs = drugRepository.findByNameContainingIgnoreCaseOrManufacturerContainingIgnoreCase(search, search);
        } else {
            drugs = drugRepository.findAll(); // Otherwise, list all drugs
        }

        model.addAttribute("drugs", drugs); // Pass the drugs list to the view
        return "drugs/list"; // Return the Thymeleaf view page to show
    }

    // ================================
    // 2. SHOW FORM TO ADD NEW DRUG
    // ================================
    @GetMapping("/new")
    public String showDrugForm(Model model) {
        model.addAttribute("drug", new Drug()); // Empty drug object for form binding
        model.addAttribute("diseases", diseasesRepository.findAll()); // List diseases for selection
        return "drugs/form"; // Return form view
    }

    // ================================
    // 3. SAVE NEW DRUG
    // ================================
    @PostMapping
    public String createDrug(@ModelAttribute Drug drug, @RequestParam(name = "diseaseId") Long diseaseId) {
        // Find disease by ID and link it to the drug
        Diseases disease = diseasesRepository.findById(diseaseId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid disease ID"));
        
        drug.setDisease(disease); // Set the disease reference in the drug
        drugRepository.save(drug); // Save drug to database
        return "redirect:/drugs"; // Redirect to drug list after saving
    }

    // ================================
    // 4. SHOW FORM TO EDIT A DRUG
    // ================================
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable(name = "id") Long id, Model model) {
        Drug drug = drugRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid drug ID"));
        
        model.addAttribute("drug", drug); // Add drug to model for form
        model.addAttribute("diseases", diseasesRepository.findAll()); // Add all diseases for dropdown
        return "drugs/form"; // Reuse same form page for editing
    }

    // ================================
    // 5. UPDATE EXISTING DRUG
    // ================================
    @PostMapping("/{id}")
    public String updateDrug(@PathVariable(name = "id") Long id, 
                           @ModelAttribute Drug drug, 
                           @RequestParam(name = "diseaseId") Long diseaseId) {
        Diseases disease = diseasesRepository.findById(diseaseId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid disease ID"));
        
        drug.setId(id); // Set the existing ID (important for updating)
        drug.setDisease(disease); // Set selected disease
        drugRepository.save(drug); // Save updated drug
        return "redirect:/drugs"; // Redirect to list
    }

    // ================================
    // 6. DELETE DRUG
    // ================================
    @GetMapping("/{id}/delete")
    public String deleteDrug(@PathVariable(name = "id") Long id) {
        drugRepository.deleteById(id); // Delete by ID
        return "redirect:/drugs"; // Go back to list
    }
}
