package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.model.Patient;
import com.example.model.Address;
import com.example.model.Diseases;
import com.example.model.Diseases.DiseaseType;
import com.example.model.Strength;
import com.example.repository.PatientRepository;
import com.example.repository.DrugRepository;
import com.example.repository.DrugSaleRepository;
import com.example.repository.DiseasesRepository;

import java.util.List;
import java.util.ArrayList;

@Controller
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DrugRepository drugRepository;

    @Autowired
    private DrugSaleRepository drugSaleRepository;

    @Autowired
    private DiseasesRepository diseasesRepository;

    @GetMapping
    public String listPatients(@RequestParam(required = false, name = "search") String search, Model model) {
        List<Patient> patients;
        if (search != null && !search.trim().isEmpty()) {
            patients = patientRepository.findByNameContainingIgnoreCaseOrPatientIdContainingIgnoreCase(search, search);
        } else {
            patients = patientRepository.findAll();
        }
        model.addAttribute("patients", patients);
        return "patients/list";
    }

    @GetMapping("/{id}")
    public String viewPatient(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
            model.addAttribute("patient", patient);
            return "patients/view";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Patient not found");
            return "redirect:/patients";
        }
    }

    @GetMapping("/{id}/edit")
    public String editPatient(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
            
            PatientForm patientForm = new PatientForm();
            patientForm.setName(patient.getName());
            patientForm.setAge(patient.getAge());
            patientForm.setIdNumber(patient.getIdNumber());
            patientForm.setDateOfBirth(patient.getDateOfBirth());
            patientForm.setPainStrength(patient.getPainStrength() != null ? patient.getPainStrength().name() : null);
            
            if (patient.getAddress() != null) {
                patientForm.setStreet(patient.getAddress().getStreet());
                patientForm.setCity(patient.getAddress().getCity());
                patientForm.setState(patient.getAddress().getState());
                patientForm.setPhoneNumber(patient.getAddress().getPhoneNumber());
            }
            
            if (patient.getDiseases() != null) {
                String[] diseaseTypes = patient.getDiseases().stream()
                    .map(d -> d.getDiseaseType().name())
                    .toArray(String[]::new);
                patientForm.setDiseases(diseaseTypes);
            }
            
            model.addAttribute("patient", patient);
            model.addAttribute("patientForm", patientForm);
            return "patients/edit";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Patient not found");
            return "redirect:/patients";
        }
    }

    @PostMapping("/{id}")
    public String updatePatient(@PathVariable("id") Long id, @ModelAttribute PatientForm form, RedirectAttributes redirectAttributes) {
        try {
            Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

            // Update basic information
            existingPatient.setName(form.getName());
            existingPatient.setAge(form.getAge());
            existingPatient.setIdNumber(form.getIdNumber());
            existingPatient.setDateOfBirth(form.getDateOfBirth());

            // Update address
            if (existingPatient.getAddress() == null) {
                existingPatient.setAddress(new Address());
            }
            existingPatient.getAddress().setStreet(form.getStreet());
            existingPatient.getAddress().setCity(form.getCity());
            existingPatient.getAddress().setState(form.getState());
            existingPatient.getAddress().setPhoneNumber(form.getPhoneNumber());

            // Update pain strength
            if (form.getPainStrength() != null && !form.getPainStrength().isEmpty()) {
                try {
                    existingPatient.setPainStrength(Strength.valueOf(form.getPainStrength().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    existingPatient.setPainStrength(Strength.MEDIUM);
                }
            }

            // Update diseases
            List<Diseases> diseases = new ArrayList<>();
            if (form.getDiseases() != null) {
                for (String diseaseType : form.getDiseases()) {
                    try {
                        Diseases disease = new Diseases(DiseaseType.valueOf(diseaseType.trim().toUpperCase()));
                        disease = diseasesRepository.save(disease);
                        diseases.add(disease);
                    } catch (Exception e) {
                        Diseases disease = new Diseases(DiseaseType.UNKNOWN);
                        disease = diseasesRepository.save(disease);
                        diseases.add(disease);
                    }
                }
            }
            existingPatient.setDiseases(diseases);

            patientRepository.save(existingPatient);
            redirectAttributes.addFlashAttribute("success", "Patient updated successfully");
            return "redirect:/patients";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Patient not found");
            return "redirect:/patients";
        }
    }

    @GetMapping("/{id}/delete")
    public String deletePatient(@PathVariable("id") Long id) {
        patientRepository.deleteById(id);
        return "redirect:/patients";
    }

    // Add this method to handle the home page statistics
    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("totalPatients", patientRepository.count());
        model.addAttribute("totalDrugs", drugRepository.count());
        model.addAttribute("totalSales", drugSaleRepository.count());
    }
} 