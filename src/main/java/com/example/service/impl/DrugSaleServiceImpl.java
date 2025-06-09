package com.example.service.impl;

import com.example.model.DrugSale;
import com.example.repository.DrugSaleRepository;
import com.example.service.DrugSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Service
@Transactional
public class DrugSaleServiceImpl implements DrugSaleService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DrugSaleRepository drugSaleRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DrugSale> getAllSales() {
        return entityManager.createQuery(
            "SELECT DISTINCT s FROM DrugSale s LEFT JOIN FETCH s.drug", 
            DrugSale.class
        ).getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public DrugSale getSaleById(Long id) {
        return entityManager.createQuery(
            "SELECT s FROM DrugSale s LEFT JOIN FETCH s.drug WHERE s.id = :id", 
            DrugSale.class
        )
        .setParameter("id", id)
        .getSingleResult();
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