package com.example.model;

import jakarta.persistence.*;

@Entity
public class Diseases {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DiseaseType diseaseType;

    public enum DiseaseType {
        DIABETES("Insulin therapy, diet control"),
        HYPERTENSION("Blood pressure medication, lifestyle changes"),
        ASTHMA("Inhalers, bronchodilators"),
        ARTHRITIS("Pain medication, physical therapy"),
        CANCER("Chemotherapy, radiation therapy"),
        HEART_DISEASE("Blood thinners, heart medication"),
        TUBERCULOSIS("Antibiotics, rest"),
        MALARIA("Antimalarial drugs, supportive care"),
        HEPATITIS("Antiviral medications, rest"),
        HIV_AIDS("Antiretroviral therapy, immune boosters"),
        COVID_19("Antiviral drugs, oxygen therapy"),
        DENGUE_FEVER("Fluid replacement, pain relievers"),
        CHOLERA("Rehydration therapy, antibiotics"),
        PNEUMONIA("Antibiotics, oxygen therapy"),
        MENINGITIS("Antibiotics, corticosteroids"),
        EPILEPSY("Anticonvulsant medications, lifestyle management"),
        ANEMIA("Iron supplements, dietary changes"),
        THYROID_DISORDER("Hormone therapy, medication"),
        OBESITY("Diet control, exercise"),
        PEPTIC_ULCER("Antacids, antibiotics"),
        URINARY_TRACT_INFECTION("Antibiotics, increased fluid intake"),
        GASTRITIS("Antacids, dietary adjustments"),
        SKIN_ALLERGY("Antihistamines, topical creams"),
        EYE_INFECTION("Antibiotic drops, hygiene care"),
        UNKNOWN("General treatment");

        private final String treatment;

        DiseaseType(String treatment) {
            this.treatment = treatment;
        }

        public String getTreatment() {
            return treatment;
        }
    }

    // No-arg constructor required by JPA
    public Diseases() {}

    public Diseases(DiseaseType diseaseType) {
        this.diseaseType = diseaseType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DiseaseType getDiseaseType() {
        return diseaseType;
    }

    public void setDiseaseType(DiseaseType diseaseType) {
        this.diseaseType = diseaseType;
    }

    @Override
    public String toString() {
        return diseaseType.name();
    }
} 