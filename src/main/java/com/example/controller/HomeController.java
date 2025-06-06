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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/")
    public String home(Model model) {
        // Add statistics to the model
        model.addAttribute("totalPatients", patientRepository.count());
        model.addAttribute("totalDrugs", drugRepository.count());
        model.addAttribute("totalSales", drugSaleRepository.count());
        return "home";
    }

    @GetMapping("/patient/new")
    public String showPatientForm(Model model) {
        model.addAttribute("patientForm", new PatientForm());
        return "patient_form";
    }

    @PostMapping("/patient/new")
    public String createPatient(@ModelAttribute PatientForm patientForm, Model model) {
        // Convert form data to Patient object
        Address address = new Address(
            patientForm.getStreet(),
            patientForm.getCity(),
            patientForm.getState()
        );
        address.setPhoneNumber(patientForm.getPhoneNumber());

        List<Diseases> diseases = new ArrayList<>();
        if (patientForm.getDiseases() != null) {
            for (String diseaseType : patientForm.getDiseases()) {
                try {
                    Diseases disease = new Diseases(DiseaseType.valueOf(diseaseType.trim().toUpperCase()));
                    disease = diseasesRepository.save(disease); // Save each disease first
                    diseases.add(disease);
                } catch (Exception e) {
                    // Handle invalid disease type
                    Diseases disease = new Diseases(DiseaseType.UNKNOWN);
                    disease = diseasesRepository.save(disease);
                    diseases.add(disease);
                }
            }
        }

        Strength painStrength;
        try {
            painStrength = Strength.valueOf(patientForm.getPainStrength().trim().toUpperCase());
        } catch (Exception e) {
            painStrength = Strength.MEDIUM;
        }

        LocalDate dob = patientForm.getDateOfBirth();
        if (dob == null) {
            dob = LocalDate.now().minusYears(patientForm.getAge());
        }

        Patient patient = new Patient(
            patientForm.getName(),
            patientForm.getAge(),
            address,
            patientForm.getIdNumber(),
            dob,
            diseases,
            "P" + System.currentTimeMillis(),
            painStrength,
            patientForm.getPhoneNumber()
        );
        
        // Save the patient to the database
        patient = patientRepository.save(patient);
        
        model.addAttribute("patient", patient);
        return "patient_success";
    }

    @PostMapping("/create")
    public String createPatient(@ModelAttribute Patient patient) {
        // Convert string to enum
        Diseases.DiseaseType diseaseType = Diseases.DiseaseType.valueOf(patient.getDiseases().get(0).toString());
        // ... rest of the code ...
        return "patient_success.html";
    }
} 