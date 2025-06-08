package com.example.controller;

import com.example.model.Pharmacist;
import com.example.model.Address;
import com.example.repository.PharmacistRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;

@Controller
@RequestMapping("/pharmacists")
public class PharmacistController {
    private static final Logger logger = LoggerFactory.getLogger(PharmacistController.class);

    @Autowired
    private PharmacistRepository pharmacistRepository;

    @GetMapping
    public String listPharmacists(@RequestParam(required = false) String search, Model model) {
        try {
            if (search != null && !search.trim().isEmpty()) {
                model.addAttribute("pharmacists", 
                    pharmacistRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(search, search));
            } else {
                model.addAttribute("pharmacists", pharmacistRepository.findByIsActive(true));
            }
            return "pharmacists/list";
        } catch (DataAccessException e) {
            logger.error("Database error while listing pharmacists: ", e);
            model.addAttribute("error", "Error accessing the database. Please try again later.");
            return "error";
        } catch (Exception e) {
            logger.error("Unexpected error while listing pharmacists: ", e);
            model.addAttribute("error", "An unexpected error occurred. Please try again later.");
            return "error";
        }
    }

    @GetMapping("/new")
    public String showPharmacistForm(Model model) {
        model.addAttribute("pharmacist", new Pharmacist());
        return "pharmacists/form";
    }

    @PostMapping("/save")
    public String savePharmacist(@Valid @ModelAttribute Pharmacist pharmacist, BindingResult result, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "pharmacists/form";
        }
        
        try {
            // Create address object from form fields
            String street = request.getParameter("address.street");
            String city = request.getParameter("address.city");
            String state = request.getParameter("address.state");
            String zipCode = request.getParameter("address.zipCode");
            
            if (street != null && city != null && state != null) {
                Address address = new Address();
                address.setStreet(street);
                address.setCity(city);
                address.setState(state);
                if (zipCode != null && !zipCode.trim().isEmpty()) {
                    address.setZipCode(zipCode);
                }
                pharmacist.setAddress(address);
            }

            // Save the pharmacist
            Pharmacist savedPharmacist = pharmacistRepository.save(pharmacist);
            
            // Add success message and pharmacist data to flash attributes
            redirectAttributes.addFlashAttribute("success", "Pharmacist created successfully");
            redirectAttributes.addFlashAttribute("pharmacist", savedPharmacist);
            
            return "redirect:/pharmacists/list";
        } catch (Exception e) {
            model.addAttribute("error", "Error creating pharmacist: " + e.getMessage());
            return "pharmacists/form";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            Pharmacist pharmacist = pharmacistRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid pharmacist ID: " + id));
            model.addAttribute("pharmacist", pharmacist);
            return "pharmacists/form";
        } catch (IllegalArgumentException e) {
            logger.error("Invalid pharmacist ID: {}", id);
            return "redirect:/pharmacists";
        } catch (DataAccessException e) {
            logger.error("Database error while retrieving pharmacist: ", e);
            model.addAttribute("error", "Error accessing the database. Please try again later.");
            return "error";
        } catch (Exception e) {
            logger.error("Unexpected error while retrieving pharmacist: ", e);
            model.addAttribute("error", "An unexpected error occurred. Please try again later.");
            return "error";
        }
    }

    @PostMapping("/{id}")
    public String updatePharmacist(@PathVariable("id") Long id, @ModelAttribute Pharmacist pharmacist, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            Pharmacist existingPharmacist = pharmacistRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pharmacist not found"));

            // Update pharmacist fields
            existingPharmacist.setFirstName(pharmacist.getFirstName());
            existingPharmacist.setLastName(pharmacist.getLastName());
            existingPharmacist.setAge(pharmacist.getAge());
            existingPharmacist.setGender(pharmacist.getGender());
            existingPharmacist.setPhoneNumber(pharmacist.getPhoneNumber());
            existingPharmacist.setLicenseNumber(pharmacist.getLicenseNumber());
            existingPharmacist.setQualification(pharmacist.getQualification());
            existingPharmacist.setExperience(pharmacist.getExperience());
            existingPharmacist.setJoiningDate(pharmacist.getJoiningDate());

            // Create address object from form fields
            String street = request.getParameter("address.street");
            String city = request.getParameter("address.city");
            String state = request.getParameter("address.state");
            String zipCode = request.getParameter("address.zipCode");
            
            if (street != null && city != null && state != null) {
                Address address = new Address();
                address.setStreet(street);
                address.setCity(city);
                address.setState(state);
                if (zipCode != null && !zipCode.trim().isEmpty()) {
                    address.setZipCode(zipCode);
                }
                existingPharmacist.setAddress(address);
            }

            pharmacistRepository.save(existingPharmacist);
            redirectAttributes.addFlashAttribute("success", "Pharmacist updated successfully");
            return "redirect:/pharmacists/list";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Pharmacist not found");
            return "redirect:/pharmacists/list";
        }
    }

    @PostMapping("/{id}/delete")
    public String deletePharmacist(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Pharmacist pharmacist = pharmacistRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid pharmacist ID: " + id));
            pharmacist.setActive(false);
            pharmacistRepository.save(pharmacist);
            redirectAttributes.addFlashAttribute("success", "Pharmacist deleted successfully!");
            return "redirect:/pharmacists";
        } catch (IllegalArgumentException e) {
            logger.error("Invalid pharmacist ID: {}", id);
            redirectAttributes.addFlashAttribute("error", "Invalid pharmacist ID.");
            return "redirect:/pharmacists";
        } catch (DataAccessException e) {
            logger.error("Database error while deleting pharmacist: ", e);
            redirectAttributes.addFlashAttribute("error", "Error deleting pharmacist. Please try again.");
            return "redirect:/pharmacists";
        } catch (Exception e) {
            logger.error("Unexpected error while deleting pharmacist: ", e);
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred. Please try again.");
            return "redirect:/pharmacists";
        }
    }
} 