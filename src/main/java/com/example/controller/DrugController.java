package com.example.controller;

// Importing required dependencies
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

// Marks this class as a Spring MVC controller
@Controller
@RequestMapping("/drugs") // Base path for all endpoints in this controller
public class DrugController {

    private static final Logger logger = LoggerFactory.getLogger(DrugController.class);
    private static final int DEFAULT_PAGE_SIZE = 10;

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
    public String listDrugs(Model model, 
                           @RequestParam(required = false) String search,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size) {
        try {
            logger.info("Listing drugs with search: {}, page: {}, size: {}", search, page, size);
            
            Pageable pageable = PageRequest.of(page, size);
            Page<Drug> drugsPage;
            
            if (search != null && !search.trim().isEmpty()) {
                drugsPage = drugRepository.findByNameContainingIgnoreCase(search, pageable);
            } else {
                drugsPage = drugRepository.findAll(pageable);
            }
            
            logger.info("Successfully retrieved {} drugs", drugsPage.getTotalElements());
            
            model.addAttribute("drugs", drugsPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", drugsPage.getTotalPages());
            model.addAttribute("totalItems", drugsPage.getTotalElements());
            model.addAttribute("search", search);
            
            return "drugs/list";
        } catch (Exception e) {
            logger.error("Error listing drugs", e);
            model.addAttribute("errorMessage", "Error loading drugs: " + e.getMessage());
            model.addAttribute("drugs", new ArrayList<>());
            return "drugs/list";
        }
    }

    // ================================
    // 2. SHOW FORM TO ADD NEW DRUG
    // ================================
    @GetMapping("/drugs/new")
    public String showDrugForm(Model model) {
        try {
            logger.info("Showing drug form");
            model.addAttribute("drug", new Drug());
            model.addAttribute("diseases", diseasesRepository.findAll());
            model.addAttribute("drugCategories", DrugCategory.values());
            return "drugs/form";
        } catch (Exception e) {
            logger.error("Error showing drug form: ", e);
            model.addAttribute("errorMessage", "Error loading form: " + e.getMessage());
            return "redirect:/drugs";
        }
    }

    // ================================
    // 3. SAVE NEW DRUG
    // ================================
    @PostMapping
    @Transactional
    public String createDrug(@Valid @ModelAttribute Drug drug, 
                           BindingResult bindingResult,
                           @RequestParam(name = "diseaseId") Long diseaseId,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        
        try {
            if (bindingResult.hasErrors()) {
                logger.error("Form validation errors: {}", bindingResult.getAllErrors());
                model.addAttribute("diseases", diseasesRepository.findAll());
                model.addAttribute("drugCategories", DrugCategory.values());
                return "drugs/form";
            }

            // Find disease by ID and link it to the drug
            Optional<Diseases> diseaseOpt = diseasesRepository.findById(diseaseId);
            if (!diseaseOpt.isPresent()) {
                throw new IllegalArgumentException("Invalid disease ID");
            }
            
            drug.setDisease(diseaseOpt.get()); // Set the disease reference in the drug
            
            // Validate drug category
            if (drug.getDrugCategory() == null) {
                bindingResult.rejectValue("drugCategory", "error.drug", "Drug category is required");
                model.addAttribute("diseases", diseasesRepository.findAll());
                model.addAttribute("drugCategories", DrugCategory.values());
                return "drugs/form";
            }

            // Set default values if not provided
            if (drug.getQuantity() == null) {
                drug.setQuantity(0);
            }
            if (drug.getPrice() == null) {
                drug.setPrice(BigDecimal.ZERO);
            }

            // Save drug to database
            Drug savedDrug = drugRepository.save(drug);
            
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
    @Transactional(readOnly = true)
    public String showEditForm(@PathVariable(name = "id") Long id, Model model) {
        try {
            Optional<Drug> drugOpt = drugRepository.findById(id);
            if (!drugOpt.isPresent()) {
                throw new IllegalArgumentException("Invalid drug ID");
            }
            
            model.addAttribute("drug", drugOpt.get());
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
    @Transactional
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

            Optional<Drug> existingDrugOpt = drugRepository.findById(id);
            if (!existingDrugOpt.isPresent()) {
                throw new IllegalArgumentException("Invalid drug ID");
            }

            Drug existingDrug = existingDrugOpt.get();

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
            Optional<Diseases> diseaseOpt = diseasesRepository.findById(diseaseId);
            if (!diseaseOpt.isPresent()) {
                throw new IllegalArgumentException("Invalid disease ID");
            }
            existingDrug.setDisease(diseaseOpt.get());

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
    @Transactional
    public String deleteDrug(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Drug> drugOpt = drugRepository.findById(id);
            if (!drugOpt.isPresent()) {
                throw new IllegalArgumentException("Invalid drug ID");
            }
            
            drugRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Drug deleted successfully!");
        } catch (Exception e) {
            logger.error("Error deleting drug: ", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting drug: " + e.getMessage());
        }
        return "redirect:/drugs"; // Go back to list
    }
}
