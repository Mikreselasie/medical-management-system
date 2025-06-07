package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.model.Drug;
import com.example.model.Diseases;
import com.example.repository.DrugRepository;
import com.example.repository.DiseasesRepository;
import java.util.Optional;
import com.example.model.DrugCategory;

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
            Optional<Diseases> diabetes = diseasesRepository.findByDiseaseType(Diseases.DiseaseType.DIABETES);
            Optional<Diseases> hypertension = diseasesRepository.findByDiseaseType(Diseases.DiseaseType.HYPERTENSION);

            // Create sample drugs
            Drug insulin = new Drug();
            insulin.setName("Insulin Regular");
            insulin.setManufacturer("Novo Nordisk");
            insulin.setQuantity(100);
            insulin.setPrice(25.99);
            insulin.setDisease(diabetes.orElse(null));
            insulin.setSideEffects("Hypoglycemia, weight gain, injection site reactions");
            insulin.setStrength("100 units/ml");
            insulin.setDrugCategory(DrugCategory.HORMONE);

            Drug metformin = new Drug();
            metformin.setName("Metformin 500mg");
            metformin.setManufacturer("Merck");
            metformin.setQuantity(200);
            metformin.setPrice(15.99);
            metformin.setDisease(diabetes.orElse(null));
            metformin.setSideEffects("Nausea, diarrhea, vitamin B12 deficiency");
            metformin.setStrength("500mg");
            metformin.setDrugCategory(DrugCategory.ANTIDIABETIC);

            Drug lisinopril = new Drug();
            lisinopril.setName("Lisinopril 10mg");
            lisinopril.setManufacturer("AstraZeneca");
            lisinopril.setQuantity(150);
            lisinopril.setPrice(12.99);
            lisinopril.setDisease(hypertension.orElse(null));
            lisinopril.setSideEffects("Dry cough, dizziness, high potassium levels");
            lisinopril.setStrength("10mg");
            lisinopril.setDrugCategory(DrugCategory.ANTIHYPERTENSIVE);

            Drug amlodipine = new Drug();
            amlodipine.setName("Amlodipine 5mg");
            amlodipine.setManufacturer("Pfizer");
            amlodipine.setQuantity(180);
            amlodipine.setPrice(18.99);
            amlodipine.setDisease(hypertension.orElse(null));
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