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
            Drug drug = new Drug();
            model.addAttribute("drug", drug);
            
            // Load diseases and sort them by type
            List<Diseases> diseases = diseasesRepository.findAll();
            if (diseases == null || diseases.isEmpty()) {
                logger.warn("No diseases found in the database. Initializing diseases...");
                // Initialize diseases if none exist
                for (Diseases.DiseaseType type : Diseases.DiseaseType.values()) {
                    Diseases disease = new Diseases(type);
                    diseasesRepository.save(disease);
                }
                diseases = diseasesRepository.findAll();
            }
            
            // Sort diseases by type name
            diseases.sort((d1, d2) -> d1.getDiseaseType().name().compareTo(d2.getDiseaseType().name()));
            
            model.addAttribute("diseases", diseases);
            model.addAttribute("drugCategories", DrugCategory.values());
            return "drugs/form";
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
    public String updateDrug(@PathVariable Long id,
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

            Drug existingDrug = drugRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid drug ID"));

            // Update drug properties
            existingDrug.setName(drug.getName());
            existingDrug.setManufacturer(drug.getManufacturer());
            existingDrug.setPrice(drug.getPrice());
            existingDrug.setQuantity(drug.getQuantity());
            existingDrug.setExpiryDate(drug.getExpiryDate());
            existingDrug.setDrugCategory(drug.getDrugCategory());
            existingDrug.setStrength(drug.getStrength());
            existingDrug.setDescription(drug.getDescription());
            existingDrug.setSideEffects(drug.getSideEffects());

            // Update disease
            Diseases disease = diseasesRepository.findById(diseaseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid disease ID"));
            existingDrug.setDisease(disease);

            drugRepository.save(existingDrug);
            
            redirectAttributes.addFlashAttribute("successMessage", "Drug updated successfully!");
            return "redirect:/drugs";
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
    public String deleteDrug(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            drugRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Drug deleted successfully!");
        } catch (Exception e) {
            logger.error("Error deleting drug: ", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting drug: " + e.getMessage());
        }
        return "redirect:/drugs"; // Go back to list
    }
}
