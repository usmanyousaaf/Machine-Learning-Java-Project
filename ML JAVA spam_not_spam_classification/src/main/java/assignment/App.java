package assignment;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.IOException;
import assignment.EmailFeatureExtractor;

public class App {
    // STEP 1 : ReadEmailsFromCSV
    public static List<String> readEmailsFromCSV(String csvFile) {
        List<String> emails = new ArrayList<>();
        int i = 0;
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length > 0 && i != 0) {
                    emails.add(nextLine[0]);
                }
                i++;
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return emails;
    }

    public static void main(String[] args) {
        String datasetFile = "src/spam_or_not_spam.csv"; // Replace with your dataset file
        List<String> emails = readEmailsFromCSV(datasetFile);

        // STEP 2
        EmailFeatureExtractor EFE = new EmailFeatureExtractor();

        for (int i = 0; i < emails.size(); i++) {
            String email = emails.get(i);
            // Extract features from the email
            int wordCounts = EFE.extractWordCount(email);
            int bigramCounts = EFE.extractBigramCount(email);
            int numCharacters = email.length();
            int numWords = email.split("\\s+").length;

            // Un-comment below section if you want to prnt features for the current email
            // on terminal
            System.out.println("index#" + (i + 1));
            System.out.println("Word Alphanumeric Counts: " + wordCounts);
            System.out.println("Bigram Counts: " + bigramCounts);
            System.out.println("Number of Characters: " + numCharacters);
            System.out.println("Number of Words: " + numWords);
            System.out.println("-------------------------");
        }

        // Saving Features into new CSV file
        EFE.saveFeaturesToCSV(emails, "src/features.csv");
        System.out.println("-------------------------");

        // SUMMARY OF FEATURES
        EFE.save_and_display_SummaryToCSV(emails, "src/summary_of_features.csv");
        System.out.println("-------------------------");


        //GUI
        
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                EmailFeatureVisualizer EFV = new EmailFeatureVisualizer();
                EFV.createAndShowGUI();
            }
        });
    }
}