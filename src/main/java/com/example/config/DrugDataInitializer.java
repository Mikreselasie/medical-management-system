// This class is responsible for initializing sample drug data in the database
// It implements CommandLineRunner to run after the application context is loaded
package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.model.Drug;
import com.example.model.Diseases;
import com.example.repository.DrugRepository;
import com.example.repository.DiseasesRepository;
import java.util.List;
import com.example.model.DrugCategory;
import java.math.BigDecimal;

@Component  // Marks this class as a Spring component for automatic detection
public class DrugDataInitializer implements CommandLineRunner {

    // Inject the DrugRepository to interact with the drug table
    @Autowired
    private DrugRepository drugRepository;

    // Inject the DiseasesRepository to interact with the diseases table
    @Autowired
    private DiseasesRepository diseasesRepository;

    @Override
    public void run(String... args) throws Exception {
        // Only initialize if the drug repository is empty to avoid duplicate data
        if (drugRepository.count() == 0) {
            // Fetch existing diseases from database to associate with drugs
            // Using DIABETES_TYPE_2 for diabetes-related drugs
            List<Diseases> diabetesList = diseasesRepository.findByDiseaseType(Diseases.DiseaseType.DIABETES_TYPE_2);
            // Fetch hypertension diseases for blood pressure medications
            List<Diseases> hypertensionList = diseasesRepository.findByDiseaseType(Diseases.DiseaseType.HYPERTENSION);
            
            // Get the first disease from each list, or null if no diseases exist
            Diseases diabetes = diabetesList.isEmpty() ? null : diabetesList.get(0);
            Diseases hypertension = hypertensionList.isEmpty() ? null : hypertensionList.get(0);

            // Create Insulin drug entry
            Drug insulin = new Drug();
            insulin.setName("Insulin Regular");  // Name of the drug
            insulin.setManufacturer("Novo Nordisk");  // Pharmaceutical company
            insulin.setQuantity(100);  // Available quantity in stock
            insulin.setPrice(new BigDecimal("25.99"));  // Price per unit
            insulin.setDisease(diabetes);  // Associate with diabetes
            insulin.setSideEffects("Hypoglycemia, weight gain, injection site reactions");  // Known side effects
            insulin.setStrength("100 units/ml");  // Drug concentration
            insulin.setDrugCategory(DrugCategory.HORMONE);  // Category of the drug

            // Create Metformin drug entry (oral diabetes medication)
            Drug metformin = new Drug();
            metformin.setName("Metformin 500mg");
            metformin.setManufacturer("Merck");
            metformin.setQuantity(200);
            metformin.setPrice(new BigDecimal("15.99"));
            metformin.setDisease(diabetes);
            metformin.setSideEffects("Nausea, diarrhea, vitamin B12 deficiency");
            metformin.setStrength("500mg");
            metformin.setDrugCategory(DrugCategory.ANTIDIABETIC);

            // Create Lisinopril drug entry (blood pressure medication)
            Drug lisinopril = new Drug();
            lisinopril.setName("Lisinopril 10mg");
            lisinopril.setManufacturer("AstraZeneca");
            lisinopril.setQuantity(150);
            lisinopril.setPrice(new BigDecimal("12.99"));
            lisinopril.setDisease(hypertension);
            lisinopril.setSideEffects("Dry cough, dizziness, high potassium levels");
            lisinopril.setStrength("10mg");
            lisinopril.setDrugCategory(DrugCategory.ANTIHYPERTENSIVE);

            // Create Amlodipine drug entry (another blood pressure medication)
            Drug amlodipine = new Drug();
            amlodipine.setName("Amlodipine 5mg");
            amlodipine.setManufacturer("Pfizer");
            amlodipine.setQuantity(180);
            amlodipine.setPrice(new BigDecimal("18.99"));
            amlodipine.setDisease(hypertension);
            amlodipine.setSideEffects("Swelling in ankles, dizziness, flushing");
            amlodipine.setStrength("5mg");
            amlodipine.setDrugCategory(DrugCategory.ANTIHYPERTENSIVE);

            // Save all created drugs to the database
            drugRepository.save(insulin);
            drugRepository.save(metformin);
            drugRepository.save(lisinopril);
            drugRepository.save(amlodipine);
        }
    }
} 