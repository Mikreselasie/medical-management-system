package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    
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
        try {
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

            // Initialize empty diseases list for patients with null diseases
            for (Patient patient : patients) {
                if (patient.getDiseases() == null) {
                    patient.setDiseases(new ArrayList<>());
                }
                // Ensure each disease has a valid diseaseType
                for (Diseases disease : patient.getDiseases()) {
                    if (disease == null || disease.getDiseaseType() == null) {
                        if (disease == null) {
                            disease = new Diseases();
                        }
                        disease.setDiseaseType(Diseases.DiseaseType.UNKNOWN);
                    }
                }
            }

            model.addAttribute("patients", patients);
            model.addAttribute("search", search);
            model.addAttribute("totalPatients", patientRepository.count());
            model.addAttribute("totalDoctors", doctorRepository.count());
            model.addAttribute("totalPharmacists", pharmacistRepository.count());
            return "index";
        } catch (Exception e) {
            logger.error("Error loading home page: ", e);
            model.addAttribute("error", "Error loading data. Please try again later.");
            model.addAttribute("patients", new ArrayList<>());
            model.addAttribute("totalPatients", 0);
            model.addAttribute("totalDoctors", 0);
            model.addAttribute("totalPharmacists", 0);
            return "index";
        }
    }

    @PostMapping("/create")
    public String createPatient(@ModelAttribute Patient patient) {
        // Convert string to enum
        Diseases.DiseaseType diseaseType = Diseases.DiseaseType.valueOf(patient.getDiseases().get(0).toString());
        // ... rest of the code ...
        return "patient_success.html";
    }
} 