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

import java.util.List;

@Controller
@RequestMapping("/drug-sales")
public class DrugSaleController {

    @Autowired
    private DrugSaleService drugSaleService;

    @Autowired
    private DrugService drugService;

    @GetMapping
    public String listSales(Model model) {
        List<DrugSale> sales = drugSaleService.getAllSales();
        model.addAttribute("sales", sales);
        return "drug-sales/list";
    }

    @GetMapping("/new")
    public String showSaleForm(Model model) {
        List<Drug> drugs = drugService.getAllDrugs();
        model.addAttribute("drugs", drugs);
        model.addAttribute("sale", new DrugSale());
        return "drug-sales/form";
    }

    @PostMapping
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
            if (sale.getTotalAmount() == null || sale.getTotalAmount().doubleValue() <= 0) {
                throw new RuntimeException("Total amount must be greater than 0");
            }

            drugSaleService.saveSale(sale);
            redirectAttributes.addFlashAttribute("successMessage", "Drug sale recorded successfully!");
            return "redirect:/drug-sales";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error recording drug sale: " + e.getMessage());
            return "redirect:/drug-sales/new";
        }
    }

    @GetMapping("/{id}")
    public String viewSale(@PathVariable Long id, Model model) {
        DrugSale sale = drugSaleService.getSaleById(id);
        model.addAttribute("sale", sale);
        return "drug-sales/view";
    }

    @GetMapping("/{id}/delete")
    public String deleteSale(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            drugSaleService.deleteSale(id);
            redirectAttributes.addFlashAttribute("successMessage", "Drug sale deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting drug sale: " + e.getMessage());
        }
        return "redirect:/drug-sales";
    }
} 