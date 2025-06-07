package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.model.Doctor;
import com.example.repository.DoctorRepository;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/doctors")
public class DoctorController {

    @Autowired
    private DoctorRepository doctorRepository;

    @GetMapping
    public String listDoctors(@RequestParam(required = false, name = "search") String search, Model model) {
        List<Doctor> doctors;
        
        if (search != null && !search.trim().isEmpty()) {
            doctors = doctorRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(search, search);
        } else {
            doctors = doctorRepository.findAll();
        }

        model.addAttribute("doctors", doctors);
        return "doctors/list";
    }

    @GetMapping("/new")
    public String showDoctorForm(Model model) {
        model.addAttribute("doctor", new Doctor());
        return "doctors/form";
    }

    @PostMapping
    public String createDoctor(@ModelAttribute Doctor doctor) {
        doctor.setJoiningDate(LocalDate.now());
        doctor.setActive(true);
        doctorRepository.save(doctor);
        return "redirect:/doctors";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable(name = "id") Long id, Model model) {
        Doctor doctor = doctorRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid doctor ID"));
        
        model.addAttribute("doctor", doctor);
        return "doctors/form";
    }

    @PostMapping("/{id}")
    public String updateDoctor(@PathVariable(name = "id") Long id, @ModelAttribute Doctor doctor) {
        doctor.setId(id);
        doctorRepository.save(doctor);
        return "redirect:/doctors";
    }

    @GetMapping("/{id}/delete")
    public String deleteDoctor(@PathVariable(name = "id") Long id) {
        Doctor doctor = doctorRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid doctor ID"));
        doctor.setActive(false);
        doctorRepository.save(doctor);
        return "redirect:/doctors";
    }
} 