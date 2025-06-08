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
public class StaffDataInitializer implements CommandLineRunner {

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
            createDoctor("John", "Smith", "john.smith@hospital.com", "+251912345678", 45, "Male", 
                "123 Main St", "Cardiology", "MD12345", "Harvard Medical School", 15),
            createDoctor("Sarah", "Johnson", "sarah.j@hospital.com", "+251923456789", 38, "Female", 
                "456 Oak Ave", "Neurology", "MD12346", "Stanford Medical School", 10),
            createDoctor("Michael", "Brown", "m.brown@hospital.com", "+251934567890", 52, "Male", 
                "789 Pine Rd", "Orthopedics", "MD12347", "Johns Hopkins", 20),
            createDoctor("Emily", "Davis", "e.davis@hospital.com", "+251945678901", 41, "Female", 
                "321 Elm St", "Pediatrics", "MD12348", "Yale Medical School", 12),
            createDoctor("Robert", "Wilson", "r.wilson@hospital.com", "+251956789012", 48, "Male", 
                "654 Maple Dr", "Dermatology", "MD12349", "UCLA Medical School", 18),
            createDoctor("Lisa", "Anderson", "l.anderson@hospital.com", "+251967890123", 36, "Female", 
                "987 Cedar Ln", "Gynecology", "MD12350", "Duke Medical School", 8),
            createDoctor("David", "Taylor", "d.taylor@hospital.com", "+251978901234", 44, "Male", 
                "147 Birch Rd", "Ophthalmology", "MD12351", "Mayo Clinic", 14),
            createDoctor("Jennifer", "Martinez", "j.martinez@hospital.com", "+251989012345", 39, "Female", 
                "258 Spruce Ave", "Endocrinology", "MD12352", "UCSF Medical School", 11),
            createDoctor("James", "Robinson", "j.robinson@hospital.com", "+251990123456", 50, "Male", 
                "369 Willow St", "Gastroenterology", "MD12353", "Columbia Medical School", 22),
            createDoctor("Patricia", "Clark", "p.clark@hospital.com", "+251901234567", 42, "Female", 
                "741 Oak Dr", "Psychiatry", "MD12354", "Cornell Medical School", 13),
            createDoctor("Thomas", "Rodriguez", "t.rodriguez@hospital.com", "+251912345678", 47, "Male", 
                "852 Pine Ln", "Urology", "MD12355", "Northwestern Medical School", 17),
            createDoctor("Mary", "Lewis", "m.lewis@hospital.com", "+251923456789", 40, "Female", 
                "963 Maple Rd", "Rheumatology", "MD12356", "Vanderbilt Medical School", 12),
            createDoctor("Charles", "Lee", "c.lee@hospital.com", "+251934567890", 45, "Male", 
                "159 Cedar Ave", "Pulmonology", "MD12357", "U Penn Medical School", 15),
            createDoctor("Elizabeth", "Walker", "e.walker@hospital.com", "+251945678901", 38, "Female", 
                "357 Birch St", "Hematology", "MD12358", "U Michigan Medical School", 10),
            createDoctor("Joseph", "Hall", "j.hall@hospital.com", "+251956789012", 51, "Male", 
                "486 Spruce Dr", "Nephrology", "MD12359", "U Washington Medical School", 19),
            createDoctor("Margaret", "Allen", "m.allen@hospital.com", "+251967890123", 43, "Female", 
                "753 Willow Ln", "Infectious Disease", "MD12360", "U Chicago Medical School", 14),
            createDoctor("Daniel", "Young", "d.young@hospital.com", "+251978901234", 46, "Male", 
                "951 Oak Rd", "Allergy", "MD12361", "U Texas Medical School", 16),
            createDoctor("Susan", "Hernandez", "s.hernandez@hospital.com", "+251989012345", 39, "Female", 
                "264 Pine Ave", "Geriatrics", "MD12362", "U Colorado Medical School", 11),
            createDoctor("Paul", "King", "p.king@hospital.com", "+251990123456", 49, "Male", 
                "852 Maple St", "Sports Medicine", "MD12363", "U Wisconsin Medical School", 18),
            createDoctor("Nancy", "Wright", "n.wright@hospital.com", "+251901234567", 41, "Female", 
                "741 Cedar Dr", "Emergency Medicine", "MD12364", "U Minnesota Medical School", 13)
        );

        doctorRepository.saveAll(doctors);
    }

    private void initializePharmacists() {
        List<Pharmacist> pharmacists = Arrays.asList(
            createPharmacist("William", "Turner", "w.turner@pharmacy.com", "+251912345678", 35, "Male", 
                "123 Pharmacy St", "PH12345", "PharmD", 8),
            createPharmacist("Rebecca", "Moore", "r.moore@pharmacy.com", "+251923456789", 32, "Female", 
                "456 Med Ave", "PH12346", "PharmD", 5),
            createPharmacist("Christopher", "White", "c.white@pharmacy.com", "+251934567890", 38, "Male", 
                "789 Drug Rd", "PH12347", "PharmD", 10)
        );

        pharmacistRepository.saveAll(pharmacists);
    }

    private Doctor createDoctor(String firstName, String lastName, String email, String phone, 
                              int age, String gender, String address, String specialization, 
                              String licenseNumber, String qualification, int experience) {
        Doctor doctor = new Doctor();
        doctor.setFirstName(firstName);
        doctor.setLastName(lastName);
        doctor.setEmail(email);
        doctor.setPhoneNumber(phone);
        doctor.setAge(age);
        doctor.setGender(Gender.valueOf(gender.toUpperCase()));
        doctor.setAddress(createAddress(address));
        doctor.setSpecialization(specialization);
        doctor.setLicenseNumber(licenseNumber);
        doctor.setQualification(qualification);
        doctor.setExperience(experience);
        return doctor;
    }

    private Pharmacist createPharmacist(String firstName, String lastName, String email, String phone, 
                                      int age, String gender, String address, String licenseNumber, 
                                      String qualification, int experience) {
        Pharmacist pharmacist = new Pharmacist();
        pharmacist.setFirstName(firstName);
        pharmacist.setLastName(lastName);
        pharmacist.setEmail(email);
        pharmacist.setPhoneNumber(phone);
        pharmacist.setAge(age);
        pharmacist.setGender(Gender.valueOf(gender.toUpperCase()));
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