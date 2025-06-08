package com.example.service.impl;

import com.example.model.DrugSale;
import com.example.repository.DrugSaleRepository;
import com.example.service.DrugSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DrugSaleServiceImpl implements DrugSaleService {

    @Autowired
    private DrugSaleRepository drugSaleRepository;

    @Override
    public List<DrugSale> getAllSales() {
        return drugSaleRepository.findAll();
    }

    @Override
    public DrugSale getSaleById(Long id) {
        return drugSaleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Drug sale not found"));
    }

    @Override
    public DrugSale saveSale(DrugSale sale) {
        if (sale.getQuantity() <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }
        if (sale.getTotalAmount() == null || sale.getTotalAmount().doubleValue() <= 0) {
            throw new RuntimeException("Total amount must be greater than 0");
        }
        return drugSaleRepository.save(sale);
    }

    @Override
    public void deleteSale(Long id) {
        drugSaleRepository.deleteById(id);
    }
} 