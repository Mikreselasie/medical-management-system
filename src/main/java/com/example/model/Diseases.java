// Entity class representing diseases in the medical system
// Maps to the 'diseases' table in the database
package com.example.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity  // Marks this class as a JPA entity
@Table(name = "diseases")  // Specifies the database table name
public class Diseases {

    @Id  // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment strategy
    private Long id;

    // Enum representing different types of diseases
    // Each type has associated treatments
    public enum DiseaseType {
        // Respiratory Diseases
        ARTHRITIS("Anti-inflammatory drugs, Immunosuppressants"),
        ASTHMA("Bronchodilators, Inhaled corticosteroids"),
        COPD("Bronchodilators, Pulmonary rehabilitation"),
        PNEUMONIA("Antibiotics, Rest, Hydration"),
        TUBERCULOSIS("Antitubercular drugs, Directly observed therapy"),

        // Cardiovascular Diseases
        HYPERTENSION("Antihypertensives, Lifestyle modifications"),
        HEART_FAILURE("ACE inhibitors, Beta-blockers, Diuretics"),
        CORONARY_ARTERY_DISEASE("Statins, Antiplatelet drugs, Beta-blockers"),
        ARRHYTHMIA("Antiarrhythmics, Pacemaker therapy"),

        // Neurological Disorders
        ALZHEIMERS("Cholinesterase inhibitors, Memantine"),
        PARKINSONS("Levodopa, Dopamine agonists"),
        MULTIPLE_SCLEROSIS("Disease-modifying therapies, Corticosteroids"),
        EPILEPSY("Antiepileptic drugs, Ketogenic diet"),

        // Endocrine Disorders
        DIABETES("Insulin therapy, Blood glucose monitoring"),  // Keeping for backward compatibility
        DIABETES_TYPE_1("Insulin therapy, Blood glucose monitoring"),
        DIABETES_TYPE_2("Oral antidiabetics, Lifestyle modifications"),
        HYPOTHYROIDISM("Levothyroxine, Thyroid hormone replacement"),
        HYPERTHYROIDISM("Antithyroid drugs, Radioactive iodine"),

        // Gastrointestinal Disorders
        CROHNS_DISEASE("Anti-inflammatory drugs, Immunosuppressants"),
        ULCERATIVE_COLITIS("Aminosalicylates, Corticosteroids"),
        CIRRHOSIS("Liver support medications, Lifestyle changes"),
        PANCREATITIS("Pain management, Enzyme replacement"),

        // Musculoskeletal Disorders
        RHEUMATOID_ARTHRITIS("DMARDs, Biologic agents"),
        OSTEOARTHRITIS("Pain relievers, Physical therapy"),
        OSTEOPOROSIS("Bisphosphonates, Calcium supplements"),
        GOUT("Colchicine, Uric acid lowering drugs"),

        // Infectious Diseases
        HIV_AIDS("Antiretroviral therapy, Opportunistic infection prophylaxis"),
        HEPATITIS_B("Antiviral drugs, Liver monitoring"),
        HEPATITIS_C("Direct-acting antivirals, Liver monitoring"),
        COVID_19("Antiviral drugs, Supportive care"),

        // Skin Conditions
        PSORIASIS("Topical treatments, Phototherapy"),
        ECZEMA("Moisturizers, Topical corticosteroids"),
        ACNE("Topical retinoids, Antibiotics"),
        DERMATITIS("Topical steroids, Antihistamines"),

        // Cancer Types
        CANCER("Surgery, Chemotherapy, Radiation therapy"),
        BREAST_CANCER("Surgery, Chemotherapy, Radiation therapy"),
        LUNG_CANCER("Surgery, Chemotherapy, Targeted therapy"),
        PROSTATE_CANCER("Hormone therapy, Radiation therapy"),
        COLON_CANCER("Surgery, Chemotherapy, Targeted therapy"),
        LIVER_CANCER("Surgery, Chemotherapy, Targeted therapy"),
        PANCREATIC_CANCER("Surgery, Chemotherapy, Targeted therapy"),
        LEUKEMIA("Chemotherapy, Stem cell transplantation"),
        LYMPHOMA("Chemotherapy, Radiation therapy, Immunotherapy"),

        // Mental Health
        DEPRESSION("Antidepressants, Psychotherapy"),
        ANXIETY("Anxiolytics, Cognitive behavioral therapy"),
        BIPOLAR_DISORDER("Mood stabilizers, Antipsychotics"),
        SCHIZOPHRENIA("Antipsychotics, Psychosocial therapy"),

        // Other Conditions
        MIGRAINE("Pain relievers, Preventive medications"),
        ANEMIA("Iron supplements, Vitamin B12"),
        UNKNOWN("Unknown treatment");  // Default case for unknown diseases

        private final String treatments;  // Stores the treatments for each disease type

        // Constructor for the enum
        DiseaseType(String treatments) {
            this.treatments = treatments;
        }

        // Getter for treatments
        public String getTreatments() {
            return treatments;
        }
    }

    @Enumerated(EnumType.STRING)  // Store enum as string in database
    @Column(nullable = false)  // This field cannot be null
    private DiseaseType diseaseType;  // The type of disease

    // Default constructor required by JPA
    public Diseases() {}

    // Constructor with disease type
    public Diseases(DiseaseType diseaseType) {
        this.diseaseType = diseaseType;
    }

    // Getters and Setters
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

    // Equals and HashCode methods
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

    // ToString method
    @Override
    public String toString() {
        return "Diseases{" +
                "id=" + id +
                ", diseaseType=" + diseaseType +
                '}';
    }
} 