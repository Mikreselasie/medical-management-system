package com.example.service.impl;

import com.example.model.Drug;
import com.example.repository.DrugRepository;
import com.example.service.DrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DrugServiceImpl implements DrugService {

    @Autowired
    private DrugRepository drugRepository;

    @Override
    public List<Drug> getAllDrugs() {
        return drugRepository.findAll();
    }

    @Override
    public Drug getDrugById(Long id) {
        return drugRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Drug not found"));
    }

    @Override
    public Drug saveDrug(Drug drug) {
        return drugRepository.save(drug);
    }

    @Override
    public void deleteDrug(Long id) {
        drugRepository.deleteById(id);
    }
} 