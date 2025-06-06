package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.model.DrugSale;
import com.example.model.Drug;
import com.example.repository.DrugSaleRepository;
import com.example.repository.DrugRepository;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@RequestMapping("/sales")
public class DrugSaleController {

    @Autowired
    private DrugSaleRepository drugSaleRepository;

    @Autowired
    private DrugRepository drugRepository;

    @GetMapping
    public String listSales(Model model) {
        model.addAttribute("sales", drugSaleRepository.findAll());
        return "sales/list";
    }

    @GetMapping("/new")
    public String showSaleForm(Model model) {
        model.addAttribute("sale", new DrugSale());
        // Only show drugs that are in stock (quantity > 0)
        model.addAttribute("drugs", drugRepository.findByQuantityGreaterThan(0));
        return "sales/form";
    }

    @PostMapping
    public String createSale(@ModelAttribute DrugSale sale, 
                           @RequestParam(name = "drugId") Long drugId,
                           RedirectAttributes redirectAttributes) {
        try {
            Drug drug = drugRepository.findById(drugId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid drug ID"));
            
            // Check if we have enough quantity
            if (drug.getQuantity() < sale.getQuantity()) {
                redirectAttributes.addFlashAttribute("error", "Not enough quantity available");
                return "redirect:/sales/new";
            }
            
            // Update drug quantity
            drug.setQuantity(drug.getQuantity() - sale.getQuantity());
            drugRepository.save(drug);
            
            // Create and save the sale
            sale.setDrug(drug);
            sale.setTotalPrice(drug.getPrice() * sale.getQuantity());
            sale.setSaleDate(LocalDateTime.now());
            drugSaleRepository.save(sale);
            
            redirectAttributes.addFlashAttribute("success", "Sale created successfully");
            return "redirect:/sales";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/sales/new";
        }
    }

    @GetMapping("/{id}")
    public String viewSale(@PathVariable(name = "id") Long id, 
                          Model model,
                          RedirectAttributes redirectAttributes) {
        try {
            DrugSale sale = drugSaleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sale not found"));
            model.addAttribute("sale", sale);
            return "sales/view";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/sales";
        }
    }

    @GetMapping("/{id}/edit")
    public String editSale(@PathVariable(name = "id") Long id, 
                          Model model,
                          RedirectAttributes redirectAttributes) {
        try {
            DrugSale sale = drugSaleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sale not found"));
            model.addAttribute("sale", sale);
            model.addAttribute("drugs", drugRepository.findAll());
            return "sales/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/sales";
        }
    }

    @PostMapping("/{id}/edit")
    public String updateSale(@PathVariable(name = "id") Long id, 
                           @ModelAttribute DrugSale updatedSale,
                           @RequestParam(name = "drugId") Long drugId,
                           RedirectAttributes redirectAttributes) {
        try {
            DrugSale existingSale = drugSaleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sale not found"));
            
            Drug drug = drugRepository.findById(drugId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid drug ID"));
            
            // Calculate quantity difference
            int quantityDifference = updatedSale.getQuantity() - existingSale.getQuantity();
            
            // Check if we have enough quantity for the update
            if (drug.getQuantity() < quantityDifference) {
                redirectAttributes.addFlashAttribute("error", "Not enough quantity available");
                return "redirect:/sales/" + id + "/edit";
            }
            
            // Update drug quantity
            drug.setQuantity(drug.getQuantity() - quantityDifference);
            drugRepository.save(drug);
            
            // Update sale details
            existingSale.setDrug(drug);
            existingSale.setQuantity(updatedSale.getQuantity());
            existingSale.setTotalPrice(drug.getPrice() * updatedSale.getQuantity());
            existingSale.setCustomerName(updatedSale.getCustomerName());
            existingSale.setCustomerPhone(updatedSale.getCustomerPhone());
            
            drugSaleRepository.save(existingSale);
            
            redirectAttributes.addFlashAttribute("success", "Sale updated successfully");
            return "redirect:/sales";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/sales/" + id + "/edit";
        }
    }

    @GetMapping("/report")
    public String showReport(Model model, 
                           @RequestParam(required = false) String startDate,
                           @RequestParam(required = false) String endDate) {
        
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;
        
        if (startDate != null && !startDate.isEmpty()) {
            startDateTime = LocalDate.parse(startDate).atStartOfDay();
        } else {
            startDateTime = LocalDateTime.now().minusDays(30).with(LocalTime.MIN);
        }
        
        if (endDate != null && !endDate.isEmpty()) {
            endDateTime = LocalDate.parse(endDate).atTime(LocalTime.MAX);
        } else {
            endDateTime = LocalDateTime.now().with(LocalTime.MAX);
        }
        
        model.addAttribute("sales", drugSaleRepository.findBySaleDateBetween(startDateTime, endDateTime));
        return "sales/report";
    }
} 