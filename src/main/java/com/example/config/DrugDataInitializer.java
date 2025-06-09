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

@Component
public class DrugDataInitializer implements CommandLineRunner {

    @Autowired
    private DrugRepository drugRepository;

    @Autowired
    private DiseasesRepository diseasesRepository;

    @Override
    public void run(String... args) throws Exception {
        // Only initialize if the drug repository is empty
        if (drugRepository.count() == 0) {
            // Get diseases from database
            List<Diseases> diabetesList = diseasesRepository.findByDiseaseType(Diseases.DiseaseType.DIABETES_TYPE_2);
            List<Diseases> hypertensionList = diseasesRepository.findByDiseaseType(Diseases.DiseaseType.HYPERTENSION);
            
            // Get the first disease from each list, or null if empty
            Diseases diabetes = diabetesList.isEmpty() ? null : diabetesList.get(0);
            Diseases hypertension = hypertensionList.isEmpty() ? null : hypertensionList.get(0);

            // Create sample drugs
            Drug insulin = new Drug();
            insulin.setName("Insulin Regular");
            insulin.setManufacturer("Novo Nordisk");
            insulin.setQuantity(100);
            insulin.setPrice(new BigDecimal("25.99"));
            insulin.setDisease(diabetes);
            insulin.setSideEffects("Hypoglycemia, weight gain, injection site reactions");
            insulin.setStrength("100 units/ml");
            insulin.setDrugCategory(DrugCategory.HORMONE);

            Drug metformin = new Drug();
            metformin.setName("Metformin 500mg");
            metformin.setManufacturer("Merck");
            metformin.setQuantity(200);
            metformin.setPrice(new BigDecimal("15.99"));
            metformin.setDisease(diabetes);
            metformin.setSideEffects("Nausea, diarrhea, vitamin B12 deficiency");
            metformin.setStrength("500mg");
            metformin.setDrugCategory(DrugCategory.ANTIDIABETIC);

            Drug lisinopril = new Drug();
            lisinopril.setName("Lisinopril 10mg");
            lisinopril.setManufacturer("AstraZeneca");
            lisinopril.setQuantity(150);
            lisinopril.setPrice(new BigDecimal("12.99"));
            lisinopril.setDisease(hypertension);
            lisinopril.setSideEffects("Dry cough, dizziness, high potassium levels");
            lisinopril.setStrength("10mg");
            lisinopril.setDrugCategory(DrugCategory.ANTIHYPERTENSIVE);

            Drug amlodipine = new Drug();
            amlodipine.setName("Amlodipine 5mg");
            amlodipine.setManufacturer("Pfizer");
            amlodipine.setQuantity(180);
            amlodipine.setPrice(new BigDecimal("18.99"));
            amlodipine.setDisease(hypertension);
            amlodipine.setSideEffects("Swelling in ankles, dizziness, flushing");
            amlodipine.setStrength("5mg");
            amlodipine.setDrugCategory(DrugCategory.ANTIHYPERTENSIVE);

            // Save all drugs
            drugRepository.save(insulin);
            drugRepository.save(metformin);
            drugRepository.save(lisinopril);
            drugRepository.save(amlodipine);
        }
    }
} 