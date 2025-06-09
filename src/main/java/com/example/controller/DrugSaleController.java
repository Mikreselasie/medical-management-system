package com.example.controller;

import com.example.model.DrugSale;
import com.example.model.Drug;
import com.example.service.DrugSaleService;
import com.example.service.DrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@Controller
@RequestMapping("/drug-sales")
public class DrugSaleController {
    private static final Logger logger = LoggerFactory.getLogger(DrugSaleController.class);

    @Autowired
    private DrugSaleService drugSaleService;

    @Autowired
    private DrugService drugService;

    @GetMapping
    @Transactional(readOnly = true)
    public String listSales(Model model) {
        try {
            List<DrugSale> sales = drugSaleService.getAllSales();
            model.addAttribute("sales", sales);
            return "drug-sales/list";
        } catch (Exception e) {
            logger.error("Error listing drug sales: ", e);
            model.addAttribute("errorMessage", "Error loading drug sales: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/new")
    @Transactional(readOnly = true)
    public String showSaleForm(Model model) {
        try {
            List<Drug> drugs = drugService.getAllDrugs();
            model.addAttribute("drugs", drugs);
            model.addAttribute("sale", new DrugSale());
            return "drug-sales/form";
        } catch (Exception e) {
            logger.error("Error showing sale form: ", e);
            model.addAttribute("errorMessage", "Error loading sale form: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping
    @Transactional
    public String saveSale(@ModelAttribute DrugSale sale, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please check the form for errors.");
            return "redirect:/drug-sales/new";
        }

        try {
            if (sale.getBuyerName() == null || sale.getBuyerName().trim().isEmpty()) {
                throw new RuntimeException("Buyer name is required");
            }
            if (sale.getBuyerPhone() == null || sale.getBuyerPhone().trim().isEmpty()) {
                throw new RuntimeException("Buyer phone number is required");
            }
            if (sale.getDrug() == null) {
                throw new RuntimeException("Please select a drug");
            }
            if (sale.getQuantity() == null || sale.getQuantity() <= 0) {
                throw new RuntimeException("Please enter a valid quantity");
            }

            // Calculate total amount
            sale.calculateTotalAmount();

            drugSaleService.saveSale(sale);
            redirectAttributes.addFlashAttribute("successMessage", "Drug sale recorded successfully!");
            return "redirect:/drug-sales";
        } catch (Exception e) {
            logger.error("Error saving drug sale: ", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error recording drug sale: " + e.getMessage());
            return "redirect:/drug-sales/new";
        }
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public String viewSale(@PathVariable Long id, Model model) {
        try {
            DrugSale sale = drugSaleService.getSaleById(id);
            model.addAttribute("sale", sale);
            return "drug-sales/view";
        } catch (EntityNotFoundException e) {
            logger.error("Drug sale not found with id: " + id, e);
            model.addAttribute("errorMessage", "Drug sale not found");
            return "error";
        } catch (Exception e) {
            logger.error("Error viewing drug sale: ", e);
            model.addAttribute("errorMessage", "Error loading drug sale: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/{id}/delete")
    @Transactional
    public String deleteSale(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            drugSaleService.deleteSale(id);
            redirectAttributes.addFlashAttribute("successMessage", "Drug sale deleted successfully!");
        } catch (EntityNotFoundException e) {
            logger.error("Drug sale not found with id: " + id, e);
            redirectAttributes.addFlashAttribute("errorMessage", "Drug sale not found");
        } catch (Exception e) {
            logger.error("Error deleting drug sale: ", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting drug sale: " + e.getMessage());
        }
        return "redirect:/drug-sales";
    }
} 