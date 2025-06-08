package com.example.service;

import com.example.model.DrugSale;
import java.util.List;

public interface DrugSaleService {
    List<DrugSale> getAllSales();
    DrugSale getSaleById(Long id);
    DrugSale saveSale(DrugSale sale);
    void deleteSale(Long id);
} 