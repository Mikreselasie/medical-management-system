package com.example.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class Diseases {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiseaseType diseaseType;

    public enum DiseaseType {
        // Respiratory Diseases
        ASTHMA("Inhalers, Bronchodilators, Corticosteroids"),
        PNEUMONIA("Antibiotics, Rest, Fluids"),
        BRONCHITIS("Antibiotics, Cough Medicine, Rest"),
        COPD("Bronchodilators, Pulmonary Rehabilitation"),
        TUBERCULOSIS("Antibiotics, Directly Observed Therapy"),
        
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
        ALZHEIMERS("Cholinesterase Inhibitors, Memantine"),
        
        // Endocrine Disorders
        DIABETES_TYPE_1("Insulin Therapy"),
        DIABETES_TYPE_2("Oral Medications, Insulin"),
        THYROID_DISORDER("Hormone Replacement, Anti-thyroid Medications"),
        ADDISONS_DISEASE("Corticosteroid Replacement"),
        
        // Gastrointestinal Disorders
        ULCER("Antacids, Antibiotics, Proton Pump Inhibitors"),
        CROHNS_DISEASE("Anti-inflammatory Drugs, Immunosuppressants"),
        IRRITABLE_BOWEL_SYNDROME("Diet Modification, Antispasmodics"),
        GASTRITIS("Antacids, Proton Pump Inhibitors"),
        
        // Musculoskeletal Disorders
        ARTHRITIS("NSAIDs, Physical Therapy, Joint Protection"),
        OSTEOPOROSIS("Calcium Supplements, Bisphosphonates"),
        FIBROMYALGIA("Pain Medications, Physical Therapy"),
        GOUT("NSAIDs, Colchicine, Allopurinol"),
        
        // Infectious Diseases
        MALARIA("Antimalarial Drugs, Prevention Measures"),
        HEPATITIS("Antiviral Medications, Supportive Care"),
        HIV_AIDS("Antiretroviral Therapy"),
        COVID_19("Vaccination, Antiviral Medications"),
        
        // Mental Health
        DEPRESSION("Antidepressants, Psychotherapy"),
        ANXIETY("Anti-anxiety Medications, Therapy"),
        BIPOLAR_DISORDER("Mood Stabilizers, Antipsychotics"),
        SCHIZOPHRENIA("Antipsychotics, Psychotherapy"),
        
        // Dermatological Conditions
        PSORIASIS("Topical Treatments, Phototherapy"),
        ECZEMA("Moisturizers, Corticosteroids"),
        ACNE("Topical Medications, Antibiotics"),
        DERMATITIS("Topical Steroids, Antihistamines"),
        
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
        THALASSEMIA("Blood Transfusions, Iron Chelation"),
        
        // Autoimmune Disorders
        LUPUS("Immunosuppressants, Anti-inflammatory Drugs"),
        RHEUMATOID_ARTHRITIS("DMARDs, Biologics"),
        CELIAC_DISEASE("Gluten-free Diet"),
        
        // Cancer Types
        BREAST_CANCER("Surgery, Chemotherapy, Radiation"),
        LUNG_CANCER("Surgery, Chemotherapy, Targeted Therapy"),
        PROSTATE_CANCER("Surgery, Hormone Therapy"),
        COLON_CANCER("Surgery, Chemotherapy"),
        LIVER_CANCER("Surgery, Targeted Therapy"),
        PANCREATIC_CANCER("Surgery, Chemotherapy"),
        OVARIAN_CANCER("Surgery, Chemotherapy"),
        LYMPHOMA("Chemotherapy, Radiation"),
        
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Diseases diseases = (Diseases) o;
        return Objects.equals(id, diseases.id) && diseaseType == diseases.diseaseType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, diseaseType);
    }

    @Override
    public String toString() {
        return diseaseType != null ? diseaseType.name() : "UNKNOWN";
    }
} 