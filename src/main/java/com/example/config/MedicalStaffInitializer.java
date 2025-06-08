package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.model.Doctor;
import com.example.model.Pharmacist;
import com.example.model.Gender;
import com.example.model.Address;
import com.example.repository.DoctorRepository;
import com.example.repository.PharmacistRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class MedicalStaffInitializer implements CommandLineRunner {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PharmacistRepository pharmacistRepository;

    @Override
    public void run(String... args) throws Exception {
        // Only initialize if both repositories are empty
        if (doctorRepository.count() == 0 && pharmacistRepository.count() == 0) {
            initializeDoctors();
            initializePharmacists();
        }
    }

    private void initializeDoctors() {
        List<Doctor> doctors = Arrays.asList(
            createDoctor("John", "Smith", "john.smith@hospital.com", "555-0101", 45, Gender.MALE, 
                "123 Main St", "Cardiology", "MD12345", "Harvard Medical School", 15),
            createDoctor("Sarah", "Johnson", "sarah.j@hospital.com", "555-0102", 38, Gender.FEMALE, 
                "456 Oak Ave", "Neurology", "MD12346", "Stanford Medical School", 10),
            createDoctor("Michael", "Brown", "m.brown@hospital.com", "555-0103", 52, Gender.MALE, 
                "789 Pine Rd", "Orthopedics", "MD12347", "Johns Hopkins", 20),
            createDoctor("Emily", "Davis", "e.davis@hospital.com", "555-0104", 41, Gender.FEMALE, 
                "321 Elm St", "Pediatrics", "MD12348", "Yale Medical School", 12),
            createDoctor("Robert", "Wilson", "r.wilson@hospital.com", "555-0105", 48, Gender.MALE, 
                "654 Maple Dr", "Dermatology", "MD12349", "UCLA Medical School", 18),
            createDoctor("Lisa", "Anderson", "l.anderson@hospital.com", "555-0106", 36, Gender.FEMALE, 
                "987 Cedar Ln", "Gynecology", "MD12350", "Duke Medical School", 8),
            createDoctor("David", "Taylor", "d.taylor@hospital.com", "555-0107", 44, Gender.MALE, 
                "147 Birch Rd", "Ophthalmology", "MD12351", "Mayo Clinic", 14),
            createDoctor("Jennifer", "Martinez", "j.martinez@hospital.com", "555-0108", 39, Gender.FEMALE, 
                "258 Spruce Ave", "Endocrinology", "MD12352", "UCSF Medical School", 11),
            createDoctor("James", "Robinson", "j.robinson@hospital.com", "555-0109", 50, Gender.MALE, 
                "369 Willow St", "Gastroenterology", "MD12353", "Columbia Medical School", 22),
            createDoctor("Patricia", "Clark", "p.clark@hospital.com", "555-0110", 42, Gender.FEMALE, 
                "741 Oak Dr", "Psychiatry", "MD12354", "Cornell Medical School", 13),
            createDoctor("Thomas", "Rodriguez", "t.rodriguez@hospital.com", "555-0111", 47, Gender.MALE, 
                "852 Pine Ln", "Urology", "MD12355", "Northwestern Medical School", 17),
            createDoctor("Mary", "Lewis", "m.lewis@hospital.com", "555-0112", 40, Gender.FEMALE, 
                "963 Maple Rd", "Rheumatology", "MD12356", "Vanderbilt Medical School", 12),
            createDoctor("Charles", "Lee", "c.lee@hospital.com", "555-0113", 45, Gender.MALE, 
                "159 Cedar Ave", "Pulmonology", "MD12357", "U Penn Medical School", 15),
            createDoctor("Elizabeth", "Walker", "e.walker@hospital.com", "555-0114", 38, Gender.FEMALE, 
                "357 Birch St", "Hematology", "MD12358", "U Michigan Medical School", 10),
            createDoctor("Joseph", "Hall", "j.hall@hospital.com", "555-0115", 51, Gender.MALE, 
                "486 Spruce Dr", "Nephrology", "MD12359", "U Washington Medical School", 19),
            createDoctor("Margaret", "Allen", "m.allen@hospital.com", "555-0116", 43, Gender.FEMALE, 
                "753 Willow Ln", "Infectious Disease", "MD12360", "U Chicago Medical School", 14),
            createDoctor("Daniel", "Young", "d.young@hospital.com", "555-0117", 46, Gender.MALE, 
                "951 Oak Rd", "Allergy", "MD12361", "U Texas Medical School", 16),
            createDoctor("Susan", "Hernandez", "s.hernandez@hospital.com", "555-0118", 39, Gender.FEMALE, 
                "264 Pine Ave", "Geriatrics", "MD12362", "U Colorado Medical School", 11),
            createDoctor("Paul", "King", "p.king@hospital.com", "555-0119", 49, Gender.MALE, 
                "852 Maple St", "Sports Medicine", "MD12363", "U Wisconsin Medical School", 18),
            createDoctor("Nancy", "Wright", "n.wright@hospital.com", "555-0120", 41, Gender.FEMALE, 
                "741 Cedar Dr", "Emergency Medicine", "MD12364", "U Minnesota Medical School", 13)
        );

        doctorRepository.saveAll(doctors);
    }

    private void initializePharmacists() {
        List<Pharmacist> pharmacists = Arrays.asList(
            createPharmacist("William", "Turner", "w.turner@pharmacy.com", "555-0201", 35, Gender.MALE, 
                "123 Pharmacy St", "PH12345", "PharmD", 8),
            createPharmacist("Rebecca", "Moore", "r.moore@pharmacy.com", "555-0202", 32, Gender.FEMALE, 
                "456 Med Ave", "PH12346", "PharmD", 5),
            createPharmacist("Christopher", "White", "c.white@pharmacy.com", "555-0203", 38, Gender.MALE, 
                "789 Drug Rd", "PH12347", "PharmD", 10)
        );

        pharmacistRepository.saveAll(pharmacists);
    }

    private Doctor createDoctor(String firstName, String lastName, String email, String phone, 
                              int age, Gender gender, String address, String specialization, 
                              String licenseNumber, String qualification, int experience) {
        Doctor doctor = new Doctor();
        doctor.setFirstName(firstName);
        doctor.setLastName(lastName);
        doctor.setEmail(email);
        doctor.setPhoneNumber(phone);
        doctor.setAge(age);
        doctor.setGender(gender);
        doctor.setAddress(createAddress(address));
        doctor.setSpecialization(specialization);
        doctor.setLicenseNumber(licenseNumber);
        doctor.setQualification(qualification);
        doctor.setExperience(experience);
        return doctor;
    }

    private Pharmacist createPharmacist(String firstName, String lastName, String email, String phone, 
                                      int age, Gender gender, String address, String licenseNumber, 
                                      String qualification, int experience) {
        Pharmacist pharmacist = new Pharmacist();
        pharmacist.setFirstName(firstName);
        pharmacist.setLastName(lastName);
        pharmacist.setEmail(email);
        pharmacist.setPhoneNumber(phone);
        pharmacist.setAge(age);
        pharmacist.setGender(gender);
        pharmacist.setAddress(createAddress(address));
        pharmacist.setLicenseNumber(licenseNumber);
        pharmacist.setQualification(qualification);
        pharmacist.setExperience(experience);
        return pharmacist;
    }

    private Address createAddress(String street) {
        Address address = new Address();
        address.setStreet(street);
        address.setCity("New York");
        address.setState("NY");
        address.setZipCode("10001");
        return address;
    }
} 