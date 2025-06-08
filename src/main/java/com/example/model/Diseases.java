package com.example.model;

import jakarta.persistence.*;

@Entity
public class Diseases {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiseaseType diseaseType = DiseaseType.UNKNOWN; // Default value

    public enum DiseaseType {
        // Respiratory Diseases
        ASTHMA("Inhalers, Bronchodilators, Corticosteroids"),
        PNEUMONIA("Antibiotics, Rest, Fluids"),
        BRONCHITIS("Antibiotics, Cough Medicine, Rest"),
        COPD("Bronchodilators, Pulmonary Rehabilitation"),
        
        // Cardiovascular Diseases
        HYPERTENSION("ACE Inhibitors, Beta Blockers, Lifestyle Changes"),
        HEART_DISEASE("Statins, Beta Blockers, Aspirin"),
        ARRHYTHMIA("Antiarrhythmics, Pacemaker"),
        CONGESTIVE_HEART_FAILURE("Diuretics, ACE Inhibitors, Beta Blockers"),
        
        // Neurological Disorders
        MIGRAINE("Triptans, Pain Relievers, Preventive Medications"),
        EPILEPSY("Anticonvulsants, Lifestyle Management"),
        PARKINSONS("Levodopa, Dopamine Agonists"),
        MULTIPLE_SCLEROSIS("Disease-modifying Drugs, Physical Therapy"),
        
        // Endocrine Disorders
        DIABETES("Insulin, Oral Medications, Diet Control"),
        THYROID_DISORDER("Hormone Replacement, Anti-thyroid Medications"),
        ADDISONS_DISEASE("Corticosteroid Replacement"),
        
        // Gastrointestinal Disorders
        ULCER("Antacids, Antibiotics, Proton Pump Inhibitors"),
        CROHNS_DISEASE("Anti-inflammatory Drugs, Immunosuppressants"),
        IRRITABLE_BOWEL_SYNDROME("Diet Modification, Antispasmodics"),
        
        // Musculoskeletal Disorders
        ARTHRITIS("NSAIDs, Physical Therapy, Joint Protection"),
        OSTEOPOROSIS("Calcium Supplements, Bisphosphonates"),
        FIBROMYALGIA("Pain Medications, Physical Therapy"),
        
        // Infectious Diseases
        TUBERCULOSIS("Antibiotics, Directly Observed Therapy"),
        MALARIA("Antimalarial Drugs, Prevention Measures"),
        HEPATITIS("Antiviral Medications, Supportive Care"),
        
        // Mental Health
        DEPRESSION("Antidepressants, Psychotherapy"),
        ANXIETY("Anti-anxiety Medications, Therapy"),
        BIPOLAR_DISORDER("Mood Stabilizers, Antipsychotics"),
        
        // Dermatological Conditions
        PSORIASIS("Topical Treatments, Phototherapy"),
        ECZEMA("Moisturizers, Corticosteroids"),
        ACNE("Topical Medications, Antibiotics"),
        
        // Eye Conditions
        GLAUCOMA("Eye Drops, Surgery"),
        CATARACTS("Surgery, Corrective Lenses"),
        MACULAR_DEGENERATION("Anti-VEGF Injections, Supplements"),
        
        // Kidney Disorders
        KIDNEY_STONES("Pain Management, Hydration"),
        KIDNEY_FAILURE("Dialysis, Transplant"),
        NEPHRITIS("Immunosuppressants, Blood Pressure Control"),
        
        // Blood Disorders
        ANEMIA("Iron Supplements, Vitamin B12"),
        LEUKEMIA("Chemotherapy, Bone Marrow Transplant"),
        HEMOPHILIA("Factor Replacement Therapy"),
        
        // Autoimmune Disorders
        LUPUS("Immunosuppressants, Anti-inflammatory Drugs"),
        RHEUMATOID_ARTHRITIS("DMARDs, Biologics"),
        CELIAC_DISEASE("Gluten-free Diet"),
        
        // Cancer Types
        BREAST_CANCER("Surgery, Chemotherapy, Radiation"),
        LUNG_CANCER("Surgery, Chemotherapy, Targeted Therapy"),
        PROSTATE_CANCER("Surgery, Hormone Therapy"),
        
        // Other Common Conditions
        ALLERGIES("Antihistamines, Avoidance"),
        OBESITY("Diet, Exercise, Medication"),
        INSOMNIA("Sleep Hygiene, Medication"),
        UNKNOWN("Consultation Required");

        private final String treatment;

        DiseaseType(String treatment) {
            this.treatment = treatment;
        }

        public String getTreatment() {
            return treatment;
        }
    }

    // No-arg constructor required by JPA
    public Diseases() {
        this.diseaseType = DiseaseType.UNKNOWN;
    }

    public Diseases(DiseaseType diseaseType) {
        this.diseaseType = diseaseType != null ? diseaseType : DiseaseType.UNKNOWN;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DiseaseType getDiseaseType() {
        return diseaseType != null ? diseaseType : DiseaseType.UNKNOWN;
    }

    public void setDiseaseType(DiseaseType diseaseType) {
        this.diseaseType = diseaseType != null ? diseaseType : DiseaseType.UNKNOWN;
    }

    @Override
    public String toString() {
        return diseaseType != null ? diseaseType.name() : DiseaseType.UNKNOWN.name();
    }
} 