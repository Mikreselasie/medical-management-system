package com.example.model;

public enum SideEffect {
    NAUSEA("Nausea"),
    VOMITING("Vomiting"),
    DROWSINESS("Drowsiness"),
    DIZZINESS("Dizziness"),
    HEADACHE("Headache"),
    DRY_MOUTH("Dry Mouth"),
    DIARRHEA("Diarrhea"),
    CONSTIPATION("Constipation"),
    STOMACH_UPSET("Stomach Upset"),
    ALLERGIC_REACTION("Allergic Reaction"),
    LOSS_OF_APPETITE("Loss of Appetite"),
    INSOMNIA("Insomnia"),
    INCREASED_HEART_RATE("Increased Heart Rate"),
    SWEATING("Sweating"),
    BLURRED_VISION("Blurred Vision"),
    RASH("Rash"),
    ITCHING("Itching"),
    MUSCLE_PAIN("Muscle Pain"),
    JOINT_PAIN("Joint Pain"),
    FATIGUE("Fatigue"),
    UNKNOWN("Unknown");

    private final String label;

    SideEffect(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
} 