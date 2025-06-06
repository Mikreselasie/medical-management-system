package com.example.model;

public class Dosage {
    private String dosageAmount;        // e.g., "500 mg"
    private String frequency;           // e.g., "Twice daily"
    private String duration;            // e.g., "7 days"
    private String ageGroup;            // e.g., "Adult", "Child", "Infant"
    private String maxDosage;           // e.g., "4 tablets per day"
    private String instructions;        // e.g., "Take after meals"

    public Dosage(String dosageAmount, String frequency,
                 String duration, String ageGroup,
                 String maxDosage, String instructions) {
        this.dosageAmount = dosageAmount;
        this.frequency = frequency;
        this.duration = duration;
        this.ageGroup = ageGroup;
        this.maxDosage = maxDosage;
        this.instructions = instructions;
    }

    // Getters and Setters
    public String getDosageAmount() { return dosageAmount; }
    public void setDosageAmount(String dosageAmount) { this.dosageAmount = dosageAmount; }
    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
    public String getAgeGroup() { return ageGroup; }
    public void setAgeGroup(String ageGroup) { this.ageGroup = ageGroup; }
    public String getMaxDosage() { return maxDosage; }
    public void setMaxDosage(String maxDosage) { this.maxDosage = maxDosage; }
    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    @Override
    public String toString() {
        return String.format("%s, %s for %s (%s). Max: %s. %s",
            dosageAmount, frequency, duration, ageGroup, maxDosage, instructions);
    }
} 