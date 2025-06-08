package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.example.model.Doctor;
import com.example.repository.DoctorRepository;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/doctors")
public class DoctorController {
    private static final Logger logger = LoggerFactory.getLogger(DoctorController.class);

    @Autowired
    private DoctorRepository doctorRepository;

    @GetMapping
    public String listDoctors(@RequestParam(required = false, name = "search") String search, Model model) {
        try {
            List<Doctor> doctors;
            
            if (search != null && !search.trim().isEmpty()) {
                doctors = doctorRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(search, search);
            } else {
                doctors = doctorRepository.findAll();
            }

            model.addAttribute("doctors", doctors);
            return "doctors/list";
        } catch (DataAccessException e) {
            logger.error("Database error while fetching doctors: ", e);
            model.addAttribute("error", "Unable to fetch doctors. Please try again later.");
            return "error";
        } catch (Exception e) {
            logger.error("Unexpected error while fetching doctors: ", e);
            model.addAttribute("error", "An unexpected error occurred. Please try again later.");
            return "error";
        }
    }

    @GetMapping("/new")
    public String showDoctorForm(Model model) {
        model.addAttribute("doctor", new Doctor());
        return "doctors/form";
    }

    @PostMapping
    public String createDoctor(@Valid @ModelAttribute Doctor doctor, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            logger.error("Validation errors while creating doctor: {}", bindingResult.getAllErrors());
            return "doctors/form";
        }

        try {
            doctor.setJoiningDate(LocalDate.now());
            doctor.setActive(true);
            doctorRepository.save(doctor);
            return "redirect:/doctors";
        } catch (DataAccessException e) {
            logger.error("Database error while creating doctor: ", e);
            model.addAttribute("error", "Unable to create doctor. Please try again later.");
            return "error";
        } catch (Exception e) {
            logger.error("Unexpected error while creating doctor: ", e);
            model.addAttribute("error", "An unexpected error occurred. Please try again later.");
            return "error";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable(name = "id") Long id, Model model) {
        try {
            Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid doctor ID"));
            
            model.addAttribute("doctor", doctor);
            return "doctors/form";
        } catch (IllegalArgumentException e) {
            logger.error("Invalid doctor ID: {}", id, e);
            model.addAttribute("error", "Doctor not found.");
            return "error";
        } catch (DataAccessException e) {
            logger.error("Database error while fetching doctor: ", e);
            model.addAttribute("error", "Unable to fetch doctor details. Please try again later.");
            return "error";
        } catch (Exception e) {
            logger.error("Unexpected error while fetching doctor: ", e);
            model.addAttribute("error", "An unexpected error occurred. Please try again later.");
            return "error";
        }
    }

    @PostMapping("/{id}")
    public String updateDoctor(@PathVariable(name = "id") Long id, @ModelAttribute Doctor doctor, Model model) {
        try {
            doctor.setId(id);
            doctorRepository.save(doctor);
            return "redirect:/doctors";
        } catch (DataAccessException e) {
            logger.error("Database error while updating doctor: ", e);
            model.addAttribute("error", "Unable to update doctor. Please try again later.");
            return "error";
        } catch (Exception e) {
            logger.error("Unexpected error while updating doctor: ", e);
            model.addAttribute("error", "An unexpected error occurred. Please try again later.");
            return "error";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteDoctor(@PathVariable(name = "id") Long id, Model model) {
        try {
            Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid doctor ID"));
            doctor.setActive(false);
            doctorRepository.save(doctor);
            return "redirect:/doctors";
        } catch (IllegalArgumentException e) {
            logger.error("Invalid doctor ID: {}", id, e);
            model.addAttribute("error", "Doctor not found.");
            return "error";
        } catch (DataAccessException e) {
            logger.error("Database error while deleting doctor: ", e);
            model.addAttribute("error", "Unable to delete doctor. Please try again later.");
            return "error";
        } catch (Exception e) {
            logger.error("Unexpected error while deleting doctor: ", e);
            model.addAttribute("error", "An unexpected error occurred. Please try again later.");
            return "error";
        }
    }
} 