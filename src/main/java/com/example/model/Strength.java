package com.example.model;

public enum Strength {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High");

    private final String description;

    Strength(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
} 