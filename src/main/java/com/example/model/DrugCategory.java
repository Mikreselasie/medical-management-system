package com.example.model;

public enum DrugCategory {
    ANALGESIC("Relieves pain (e.g., paracetamol, ibuprofen)"),
    ANTIBIOTIC("Kills or inhibits bacteria (e.g., amoxicillin)"),
    ANTIVIRAL("Treats viral infections (e.g., acyclovir)"),
    ANTIFUNGAL("Treats fungal infections (e.g., clotrimazole)"),
    ANTIPARASITIC("Treats parasitic infections (e.g., albendazole)"),
    ANTIPYRETIC("Reduces fever (e.g., paracetamol)"),
    ANTIINFLAMMATORY("Reduces inflammation (e.g., ibuprofen)"),
    ANTIHYPERTENSIVE("Lowers high blood pressure (e.g., amlodipine)"),
    DIURETIC("Increases urine output (e.g., furosemide)"),
    ANTIDEPRESSANT("Treats depression or anxiety (e.g., fluoxetine)"),
    ANTIPSYCHOTIC("Treats severe mental disorders (e.g., risperidone)"),
    ANXIOLYTIC("Reduces anxiety or helps sleep (e.g., diazepam)"),
    ANTIEPILEPTIC("Controls seizures (e.g., valproic acid)"),
    BRONCHODILATOR("Opens airways in asthma/COPD (e.g., salbutamol)"),
    HORMONE("Hormonal treatments (e.g., insulin, estrogen)"),
    LAXATIVE("Relieves constipation (e.g., lactulose)"),
    ANTIDIABETIC("Controls blood sugar (e.g., metformin)"),
    ANTIHISTAMINE("Treats allergies (e.g., cetirizine)"),
    COUGH_MEDICATION("Relieves cough or mucus (e.g., codeine, guaifenesin)"),
    VITAMIN("Supports health or deficiency (e.g., vitamin D)"),
    OTHER("Other");

    private final String description;

    DrugCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
} 