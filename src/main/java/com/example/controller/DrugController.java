package com.example.controller;

// Importing required dependencies
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.model.Drug;
import com.example.model.Diseases;
import com.example.model.DrugCategory;
import com.example.repository.DrugRepository;
import com.example.repository.DiseasesRepository;

import jakarta.validation.Valid;
import java.util.List;

// Marks this class as a Spring MVC controller
@Controller
@RequestMapping("/drugs") // Base path for all endpoints in this controller
public class DrugController {

    private static final Logger logger = LoggerFactory.getLogger(DrugController.class);

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
        try {
            List<Drug> drugs;
            
            // If a search query is given, filter drugs by name or manufacturer
            if (search != null && !search.trim().isEmpty()) {
                drugs = drugRepository.findByNameContainingIgnoreCaseOrManufacturerContainingIgnoreCase(search, search);
            } else {
                drugs = drugRepository.findAll(); // Otherwise, list all drugs
            }

            model.addAttribute("drugs", drugs); // Pass the drugs list to the view
            model.addAttribute("search", search);
            return "drugs/list"; // Return the Thymeleaf view page to show
        } catch (Exception e) {
            logger.error("Error listing drugs: ", e);
            model.addAttribute("errorMessage", "Error loading drugs. Please try again later.");
            return "drugs/list";
        }
    }

    // ================================
    // 2. SHOW FORM TO ADD NEW DRUG
    // ================================
    @GetMapping("/new")
    public String showDrugForm(Model model) {
        try {
            model.addAttribute("drug", new Drug()); // Empty drug object for form binding
            model.addAttribute("diseases", diseasesRepository.findAll()); // List diseases for selection
            model.addAttribute("drugCategories", DrugCategory.values());
            return "drugs/form"; // Return form view
        } catch (Exception e) {
            logger.error("Error showing drug form: ", e);
            return "redirect:/drugs?error=Error loading form";
        }
    }

    // ================================
    // 3. SAVE NEW DRUG
    // ================================
    @PostMapping
    public String createDrug(@Valid @ModelAttribute Drug drug, 
                           BindingResult bindingResult,
                           @RequestParam(name = "diseaseId") Long diseaseId,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        
        try {
            if (bindingResult.hasErrors()) {
                model.addAttribute("diseases", diseasesRepository.findAll());
                model.addAttribute("drugCategories", DrugCategory.values());
                return "drugs/form";
            }

            // Find disease by ID and link it to the drug
            Diseases disease = diseasesRepository.findById(diseaseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid disease ID"));
            
            drug.setDisease(disease); // Set the disease reference in the drug
            
            // Validate drug category
            if (drug.getDrugCategory() == null) {
                bindingResult.rejectValue("drugCategory", "error.drug", "Drug category is required");
                model.addAttribute("diseases", diseasesRepository.findAll());
                model.addAttribute("drugCategories", DrugCategory.values());
                return "drugs/form";
            }

            drugRepository.save(drug); // Save drug to database
            
            redirectAttributes.addFlashAttribute("successMessage", "Drug added successfully!");
            return "redirect:/drugs"; // Redirect to drug list after saving
        } catch (Exception e) {
            logger.error("Error creating drug: ", e);
            model.addAttribute("errorMessage", "Error saving drug: " + e.getMessage());
            model.addAttribute("diseases", diseasesRepository.findAll());
            model.addAttribute("drugCategories", DrugCategory.values());
            return "drugs/form";
        }
    }

    // ================================
    // 4. SHOW FORM TO EDIT A DRUG
    // ================================
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable(name = "id") Long id, Model model) {
        try {
            Drug drug = drugRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid drug ID"));
            
            model.addAttribute("drug", drug); // Add drug to model for form
            model.addAttribute("diseases", diseasesRepository.findAll()); // Add all diseases for dropdown
            model.addAttribute("drugCategories", DrugCategory.values());
            return "drugs/form"; // Reuse same form page for editing
        } catch (Exception e) {
            logger.error("Error showing edit form: ", e);
            return "redirect:/drugs?error=" + e.getMessage();
        }
    }

    // ================================
    // 5. UPDATE EXISTING DRUG
    // ================================
    @PostMapping("/{id}")
    public String updateDrug(@PathVariable(name = "id") Long id, 
                           @Valid @ModelAttribute Drug drug,
                           BindingResult bindingResult,
                           @RequestParam(name = "diseaseId") Long diseaseId,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        
        try {
            if (bindingResult.hasErrors()) {
                model.addAttribute("diseases", diseasesRepository.findAll());
                model.addAttribute("drugCategories", DrugCategory.values());
                return "drugs/form";
            }

            Diseases disease = diseasesRepository.findById(diseaseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid disease ID"));
            
            drug.setId(id); // Set the existing ID (important for updating)
            drug.setDisease(disease); // Set selected disease
            
            // Validate drug category
            if (drug.getDrugCategory() == null) {
                bindingResult.rejectValue("drugCategory", "error.drug", "Drug category is required");
                model.addAttribute("diseases", diseasesRepository.findAll());
                model.addAttribute("drugCategories", DrugCategory.values());
                return "drugs/form";
            }

            drugRepository.save(drug); // Save updated drug
            
            redirectAttributes.addFlashAttribute("successMessage", "Drug updated successfully!");
            return "redirect:/drugs"; // Redirect to list
        } catch (Exception e) {
            logger.error("Error updating drug: ", e);
            model.addAttribute("errorMessage", "Error updating drug: " + e.getMessage());
            model.addAttribute("diseases", diseasesRepository.findAll());
            model.addAttribute("drugCategories", DrugCategory.values());
            return "drugs/form";
        }
    }

    // ================================
    // 6. DELETE DRUG
    // ================================
    @GetMapping("/{id}/delete")
    public String deleteDrug(@PathVariable(name = "id") Long id, RedirectAttributes redirectAttributes) {
        try {
            drugRepository.deleteById(id); // Delete by ID
            redirectAttributes.addFlashAttribute("successMessage", "Drug deleted successfully!");
        } catch (Exception e) {
            logger.error("Error deleting drug: ", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting drug: " + e.getMessage());
        }
        return "redirect:/drugs"; // Go back to list
    }
}
