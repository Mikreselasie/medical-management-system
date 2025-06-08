package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.model.Address;
import com.example.model.Diseases;
import com.example.model.Diseases.DiseaseType;
import com.example.model.Patient;
import com.example.model.Strength;
import com.example.repository.PatientRepository;
import com.example.repository.DiseasesRepository;
import com.example.repository.DrugRepository;
import com.example.repository.DrugSaleRepository;
import com.example.repository.DoctorRepository;
import com.example.repository.PharmacistRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DiseasesRepository diseasesRepository;

    @Autowired
    private DrugRepository drugRepository;

    @Autowired
    private DrugSaleRepository drugSaleRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PharmacistRepository pharmacistRepository;

    @GetMapping("/")
    public String home(@RequestParam(required = false) String search, Model model) {
        List<Patient> patients;
        if (search != null && !search.trim().isEmpty()) {
            // Split the search term into first and last name
            String[] nameParts = search.trim().split("\\s+", 2);
            String firstName = nameParts[0];
            String lastName = nameParts.length > 1 ? nameParts[1] : nameParts[0];
            
            patients = patientRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrPatientIdContainingIgnoreCase(
                firstName, lastName, search);
        } else {
            patients = patientRepository.findAll();
        }
        model.addAttribute("patients", patients);
        model.addAttribute("search", search);
        model.addAttribute("totalPatients", patientRepository.count());
        model.addAttribute("totalDoctors", doctorRepository.count());
        model.addAttribute("totalPharmacists", pharmacistRepository.count());
        return "index";
    }

    @PostMapping("/create")
    public String createPatient(@ModelAttribute Patient patient) {
        // Convert string to enum
        Diseases.DiseaseType diseaseType = Diseases.DiseaseType.valueOf(patient.getDiseases().get(0).toString());
        // ... rest of the code ...
        return "patient_success.html";
    }
} 