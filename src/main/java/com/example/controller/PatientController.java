package com.example.controller;

import com.example.model.*;
import com.example.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/patients")
public class PatientController {
    private static final Logger logger = LoggerFactory.getLogger(PatientController.class);

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DiseasesRepository diseasesRepository;

    @GetMapping("/list")
    public String listPatients(Model model) {
        try {
            List<Patient> patients = patientRepository.findAll();
            if (patients == null) {
                patients = new ArrayList<>();
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
            return "patients/list";
        } catch (Exception e) {
            logger.error("Error fetching patients: ", e);
            model.addAttribute("error", "Error loading patients. Please try again later.");
            model.addAttribute("patients", new ArrayList<>());
            return "patients/list";
        }
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        try {
            model.addAttribute("patient", new Patient());
            List<Diseases> diseases = diseasesRepository.findAll();
            if (diseases == null || diseases.isEmpty()) {
                logger.warn("No diseases found in the database. Initializing diseases...");
                // Initialize diseases if none exist
                for (Diseases.DiseaseType type : Diseases.DiseaseType.values()) {
                    Diseases disease = new Diseases(type);
                    diseasesRepository.save(disease);
                }
                diseases = diseasesRepository.findAll();
            }
            model.addAttribute("diseases", diseases);
            model.addAttribute("genders", Gender.values());
            model.addAttribute("strengths", Strength.values());
            return "patients/form";
        } catch (Exception e) {
            logger.error("Error showing create form: ", e);
            model.addAttribute("error", "Error loading form. Please try again later.");
            return "redirect:/patients/list";
        }
    }

    @PostMapping("/save")
    public String savePatient(@Valid @ModelAttribute Patient patient, BindingResult result, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            try {
                model.addAttribute("diseases", diseasesRepository.findAll());
                model.addAttribute("genders", Gender.values());
                model.addAttribute("strengths", Strength.values());
                return "patients/form";
            } catch (Exception e) {
                logger.error("Error loading form data: ", e);
                model.addAttribute("error", "Error loading form data. Please try again later.");
                return "redirect:/patients/list";
            }
        }
        
        try {
            // Generate a unique patient ID
            String patientId = "P" + String.format("%03d", patientRepository.count() + 1);
            patient.setPatientId(patientId);

            // Convert pain strength from string to enum
            String painStrengthStr = request.getParameter("painStrength");
            if (painStrengthStr != null && !painStrengthStr.isEmpty()) {
                try {
                    Strength painStrength = Strength.valueOf(painStrengthStr);
                    patient.setPainStrength(painStrength);
                } catch (IllegalArgumentException e) {
                    patient.setPainStrength(Strength.MEDIUM); // Default value
                }
            }

            // Calculate date of birth from age
            int age = patient.getAge();
            LocalDate currentDate = LocalDate.now();
            LocalDate dateOfBirth = currentDate.minusYears(age);
            patient.setDateOfBirth(dateOfBirth);

            // Handle diseases
            String[] diseaseIds = request.getParameterValues("diseases");
            List<Diseases> diseases = new ArrayList<>();
            if (diseaseIds != null && diseaseIds.length > 0) {
                for (String diseaseId : diseaseIds) {
                    try {
                        Long id = Long.parseLong(diseaseId);
                        Optional<Diseases> disease = diseasesRepository.findById(id);
                        if (disease.isPresent()) {
                            Diseases d = disease.get();
                            if (d.getDiseaseType() == null) {
                                d.setDiseaseType(Diseases.DiseaseType.UNKNOWN);
                            }
                            diseases.add(d);
                        }
                    } catch (NumberFormatException e) {
                        logger.warn("Invalid disease ID: {}", diseaseId);
                    }
                }
            }
            patient.setDiseases(diseases);

            // Handle address
            Address address = new Address();
            address.setStreet(request.getParameter("address.street"));
            address.setCity(request.getParameter("address.city"));
            address.setState(request.getParameter("address.state"));
            address.setZipCode(request.getParameter("address.zipCode"));
            patient.setAddress(address);

            // Save the patient
            Patient savedPatient = patientRepository.save(patient);
            
            // Add success message and patient data to flash attributes
            redirectAttributes.addFlashAttribute("success", "Patient registered successfully");
            redirectAttributes.addFlashAttribute("patient", savedPatient);
            
            return "redirect:/patients/list";
        } catch (Exception e) {
            logger.error("Error saving patient: ", e);
            model.addAttribute("error", "Error registering patient: " + e.getMessage());
            try {
                model.addAttribute("diseases", diseasesRepository.findAll());
                model.addAttribute("genders", Gender.values());
                model.addAttribute("strengths", Strength.values());
            } catch (Exception ex) {
                logger.error("Error loading form data: ", ex);
            }
            return "patients/form";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            Optional<Patient> patient = patientRepository.findById(id);
            if (patient.isPresent()) {
                Patient p = patient.get();
                if (p.getDiseases() == null) {
                    p.setDiseases(new ArrayList<>());
                }
                // Ensure each disease has a valid diseaseType
                for (Diseases disease : p.getDiseases()) {
                    if (disease.getDiseaseType() == null) {
                        disease.setDiseaseType(Diseases.DiseaseType.UNKNOWN);
                    }
                }
                model.addAttribute("patient", p);
                model.addAttribute("diseases", diseasesRepository.findAll());
                model.addAttribute("genders", Gender.values());
                model.addAttribute("strengths", Strength.values());
                return "patients/form";
            }
            return "redirect:/patients/list";
        } catch (Exception e) {
            logger.error("Error showing edit form: ", e);
            model.addAttribute("error", "Error loading form. Please try again later.");
            return "redirect:/patients/list";
        }
    }

    @PostMapping("/update/{id}")
    public String updatePatient(@PathVariable Long id, @Valid @ModelAttribute Patient patient, BindingResult result, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            try {
                model.addAttribute("diseases", diseasesRepository.findAll());
                model.addAttribute("genders", Gender.values());
                model.addAttribute("strengths", Strength.values());
                return "patients/form";
            } catch (Exception e) {
                logger.error("Error loading form data: ", e);
                model.addAttribute("error", "Error loading form data. Please try again later.");
                return "redirect:/patients/list";
            }
        }

        try {
            Optional<Patient> existingPatient = patientRepository.findById(id);
            if (existingPatient.isPresent()) {
                Patient updatedPatient = existingPatient.get();
                
                // Update basic information
                updatedPatient.setFirstName(patient.getFirstName());
                updatedPatient.setLastName(patient.getLastName());
                updatedPatient.setAge(patient.getAge());
                updatedPatient.setGender(patient.getGender());
                updatedPatient.setPhoneNumber(patient.getPhoneNumber());
                updatedPatient.setEmail(patient.getEmail());

                // Update pain strength
                String painStrengthStr = request.getParameter("painStrength");
                if (painStrengthStr != null && !painStrengthStr.isEmpty()) {
                    try {
                        Strength painStrength = Strength.valueOf(painStrengthStr);
                        updatedPatient.setPainStrength(painStrength);
                    } catch (IllegalArgumentException e) {
                        updatedPatient.setPainStrength(Strength.MEDIUM);
                    }
                }

                // Update date of birth
                LocalDate currentDate = LocalDate.now();
                LocalDate dateOfBirth = currentDate.minusYears(patient.getAge());
                updatedPatient.setDateOfBirth(dateOfBirth);

                // Update diseases
                String[] diseaseIds = request.getParameterValues("diseases");
                List<Diseases> diseases = new ArrayList<>();
                if (diseaseIds != null && diseaseIds.length > 0) {
                    for (String diseaseId : diseaseIds) {
                        try {
                            Long diseaseIdLong = Long.parseLong(diseaseId);
                            Optional<Diseases> disease = diseasesRepository.findById(diseaseIdLong);
                            if (disease.isPresent()) {
                                Diseases d = disease.get();
                                if (d.getDiseaseType() == null) {
                                    d.setDiseaseType(Diseases.DiseaseType.UNKNOWN);
                                }
                                diseases.add(d);
                            }
                        } catch (NumberFormatException e) {
                            logger.warn("Invalid disease ID: {}", diseaseId);
                        }
                    }
                }
                updatedPatient.setDiseases(diseases);

                // Update address
                String street = request.getParameter("address.street");
                String city = request.getParameter("address.city");
                String state = request.getParameter("address.state");
                String zipCode = request.getParameter("address.zipCode");
                
                if (street != null && city != null && state != null) {
                    Address address = new Address();
                    address.setStreet(street);
                    address.setCity(city);
                    address.setState(state);
                    if (zipCode != null && !zipCode.trim().isEmpty()) {
                        address.setZipCode(zipCode);
                    }
                    updatedPatient.setAddress(address);
                }

                // Save the updated patient
                Patient savedPatient = patientRepository.save(updatedPatient);
                
                redirectAttributes.addFlashAttribute("success", "Patient updated successfully");
                redirectAttributes.addFlashAttribute("patient", savedPatient);
                
                return "redirect:/patients/list";
            }
            return "redirect:/patients/list";
        } catch (Exception e) {
            logger.error("Error updating patient: ", e);
            model.addAttribute("error", "Error updating patient: " + e.getMessage());
            try {
                model.addAttribute("diseases", diseasesRepository.findAll());
                model.addAttribute("genders", Gender.values());
                model.addAttribute("strengths", Strength.values());
            } catch (Exception ex) {
                logger.error("Error loading form data: ", ex);
            }
            return "patients/form";
        }
    }

    @GetMapping("/delete/{id}")
    public String deletePatient(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            patientRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Patient deleted successfully");
        } catch (Exception e) {
            logger.error("Error deleting patient: ", e);
            redirectAttributes.addFlashAttribute("error", "Error deleting patient: " + e.getMessage());
        }
        return "redirect:/patients/list";
    }

    @GetMapping
    public String redirectToList() {
        return "redirect:/patients/list";
    }

    @GetMapping("/{id}")
    public String viewPatient(@PathVariable Long id, Model model) {
        try {
            Optional<Patient> patient = patientRepository.findById(id);
            if (patient.isPresent()) {
                Patient p = patient.get();
                if (p.getDiseases() == null) {
                    p.setDiseases(new ArrayList<>());
                }
                // Ensure each disease has a valid diseaseType
                for (Diseases disease : p.getDiseases()) {
                    if (disease.getDiseaseType() == null) {
                        disease.setDiseaseType(Diseases.DiseaseType.UNKNOWN);
                    }
                }
                model.addAttribute("patient", p);
                return "patients/view";
            }
            return "redirect:/patients/list";
        } catch (Exception e) {
            logger.error("Error viewing patient: ", e);
            model.addAttribute("error", "Error loading patient details. Please try again later.");
            return "redirect:/patients/list";
        }
    }
} 