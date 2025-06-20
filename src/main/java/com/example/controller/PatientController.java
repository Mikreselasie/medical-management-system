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
    public String listPatients(@RequestParam(required = false) String search, Model model) {
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
                        try {
                            // This will throw an exception if the disease type is invalid
                            disease.getDiseaseType();
                        } catch (Exception e) {
                            logger.warn("Found invalid disease type in patient record: {}", disease);
                            disease.setDiseaseType(Diseases.DiseaseType.UNKNOWN);
                            diseasesRepository.save(disease);
                        }
                    }
                }
            }
            model.addAttribute("patients", patients);
            model.addAttribute("search", search);
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
            Patient patient = new Patient();
            model.addAttribute("patient", patient);
            
            // Load diseases and handle any invalid types
            List<Diseases> diseases = diseasesRepository.findAll();
            boolean hasInvalidTypes = false;
            
            for (Diseases disease : diseases) {
                try {
                    // This will throw an exception if the disease type is invalid
                    disease.getDiseaseType();
                } catch (Exception e) {
                    logger.warn("Found invalid disease type in database: {}", disease);
                    hasInvalidTypes = true;
                    // Set to UNKNOWN type
                    disease.setDiseaseType(Diseases.DiseaseType.UNKNOWN);
                    diseasesRepository.save(disease);
                }
            }
            
            if (hasInvalidTypes) {
                logger.info("Fixed invalid disease types in database");
                diseases = diseasesRepository.findAll();
            }
            
            // Sort diseases by type name
            diseases.sort((d1, d2) -> d1.getDiseaseType().name().compareTo(d2.getDiseaseType().name()));
            
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
    public String savePatient(@Valid @ModelAttribute Patient patient,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes,
                             HttpServletRequest request) {
        try {
            if (result.hasErrors()) {
                logger.error("Validation errors: {}", result.getAllErrors());
                // Add necessary attributes for form re-rendering
                model.addAttribute("diseases", diseasesRepository.findAll());
                model.addAttribute("genders", Gender.values());
                model.addAttribute("strengths", Strength.values());
                return "patients/form";
            }

            // Initialize address if null
            if (patient.getAddress() == null) {
                patient.setAddress(new Address());
            }

            // Initialize diseases list
            if (patient.getDiseases() == null) {
                patient.setDiseases(new ArrayList<>());
            }

            // Handle diseases selection
            String[] diseaseIds = request.getParameterValues("diseases");
            if (diseaseIds != null && diseaseIds.length > 0) {
                for (String diseaseId : diseaseIds) {
                    try {
                        Long diseaseIdLong = Long.parseLong(diseaseId);
                        Optional<Diseases> disease = diseasesRepository.findById(diseaseIdLong);
                        if (disease.isPresent()) {
                            patient.getDiseases().add(disease.get());
                        }
                    } catch (NumberFormatException e) {
                        logger.warn("Invalid disease ID: {}", diseaseId);
                    }
                }
            }

            // Generate patient ID if not set
            if (patient.getPatientId() == null || patient.getPatientId().trim().isEmpty()) {
                long patientCount = patientRepository.count() + 1;
                String patientId = String.format("P%06d", patientCount);
                patient.setPatientId(patientId);
            }

            // Set default pain strength if not set
            if (patient.getPainStrength() == null) {
                patient.setPainStrength(Strength.MEDIUM);
            }

            // Save the patient
            Patient savedPatient = patientRepository.save(patient);
            logger.info("Successfully saved patient with ID: {}", savedPatient.getId());
            redirectAttributes.addFlashAttribute("success", "Patient saved successfully");
            return "redirect:/patients/list";
        } catch (Exception e) {
            logger.error("Error saving patient: ", e);
            model.addAttribute("error", "Error saving patient: " + e.getMessage());
            model.addAttribute("diseases", diseasesRepository.findAll());
            model.addAttribute("genders", Gender.values());
            model.addAttribute("strengths", Strength.values());
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
    public String updatePatient(@PathVariable Long id, @Valid @ModelAttribute Patient patient, 
                              BindingResult result, Model model, HttpServletRequest request, 
                              RedirectAttributes redirectAttributes) {
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
                                diseases.add(disease.get());
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

    @GetMapping("/{id}/delete")
    public String deletePatient(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Patient> patientOpt = patientRepository.findById(id);
            if (patientOpt.isPresent()) {
                Patient patient = patientOpt.get();
                // Clear the diseases list first to remove the associations
                patient.getDiseases().clear();
                patientRepository.save(patient);
                // Now we can safely delete the patient
                patientRepository.deleteById(id);
                redirectAttributes.addFlashAttribute("success", "Patient deleted successfully!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Patient not found!");
            }
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