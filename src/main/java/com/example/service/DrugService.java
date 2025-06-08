package com.example.service;

import com.example.model.Drug;
import java.util.List;

public interface DrugService {
    List<Drug> getAllDrugs();
    Drug getDrugById(Long id);
    Drug saveDrug(Drug drug);
    void deleteDrug(Long id);
} 