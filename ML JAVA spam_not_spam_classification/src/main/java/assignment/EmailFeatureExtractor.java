package assignment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import com.opencsv.CSVWriter;
import java.util.regex.Matcher;

public class EmailFeatureExtractor {

    public int extractWordCount(String email) {
        String[] words = email.split("\\s+");
        int totalWordCount = 0;
        for (String word : words) {
            // Remove non-alphanumeric characters and convert to lowercase
            word = word.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
            if (!word.isEmpty()) {
                totalWordCount++;
            }
        }
        return totalWordCount;
    }

    public int extractBigramCount(String email) {
        String[] words = email.split("\\s+");
        int totalBigramCount = 0;
        for (int i = 0; i < words.length - 1; i++) {
            String bigram = words[i] + " " + words[i + 1];
            totalBigramCount++;
        }
        return totalBigramCount;
    }

    public int countSentences(String text) {
        String[] sentences = text.split("[.!?]");
        return sentences.length;
    }

    public void saveFeaturesToCSV(List<String> emails, String outputPath) {
        try (FileWriter writer = new FileWriter(outputPath);
                CSVWriter csvWriter = new CSVWriter(writer)) {

            // Define the CSV header
            String[] header = { "Index", "Word Alphanumeric Counts", "Bigram Counts", "Number of Characters",
                    "Number of Words" };
            csvWriter.writeNext(header);

            for (int i = 0; i < emails.size(); i++) {
                String email = emails.get(i);
                int wordCounts = extractWordCount(email);
                int bigramCounts = extractBigramCount(email);
                int numCharacters = email.length();
                int numWords = email.split("\\s+").length;

                // Create a string array for the current row
                String[] row = {
                        String.valueOf(i + 1),
                        String.valueOf(wordCounts),
                        String.valueOf(bigramCounts),
                        String.valueOf(numCharacters),
                        String.valueOf(numWords),
                };

                // Write the row to the CSV file
                csvWriter.writeNext(row);
            }

            System.out.println("FEATURED SAVED SUCCESSFULLY IN : " + outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // FEATURE SUMMARY CALCULATION

    public double calculateMedian(List<Integer> list) {
        Collections.sort(list);
        int size = list.size();
        if (size % 2 == 0) {
            // If the list has an even number of elements, return the average of the middle
            // two
            int middle1 = list.get(size / 2 - 1);
            int middle2 = list.get(size / 2);
            return (double) (middle1 + middle2) / 2.0;
        } else {
            // If the list has an odd number of elements, return the middle element
            return (double) list.get(size / 2);
        }
    }

    public double calculateStandardDeviation(List<Integer> list) {
        double mean = calculateMean(list);
        int size = list.size();

        // Calculate the sum of squared differences from the mean
        double sumSquaredDiff = 0;
        for (int value : list) {
            double diff = value - mean;
            sumSquaredDiff += diff * diff;
        }

        // Calculate the variance and return the square root to get the standard
        // deviation
        double variance = sumSquaredDiff / size;
        return Math.sqrt(variance);
    }

    public double calculateMean(List<Integer> values) {
        if (values.isEmpty()) {
            return 0.0; // You can return any default value you prefer for an empty list
        }

        int sum = 0;
        for (int value : values) {
            sum += value;
        }

        return (double) sum / values.size();
    }

    public Map<String, Integer> computeWordFrequency(List<String> emails) {
        Map<String, Integer> wordFrequency = new HashMap<>();

        for (String email : emails) {
            String[] words = email.split("\\s+");
            for (String word : words) {
                word = word.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
                if (!word.isEmpty()) {
                    wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
                }
            }
        }

        return wordFrequency;
    }

    // Helper methods for min, max, mean
    private int min(List<Integer> list) {
        int min = Integer.MAX_VALUE;
        for (int num : list) {
            if (num < min) {
                min = num;
            }
        }
        return min;
    }

    private int max(List<Integer> list) {
        int max = Integer.MIN_VALUE;
        for (int num : list) {
            if (num > max) {
                max = num;
            }
        }
        return max;
    }

    private double mean(List<Integer> list) {
        int sum = 0;
        for (int num : list) {
            sum += num;
        }
        return (double) sum / list.size();
    }

    public List<String> extractCommonWords(List<String> emails) {
        // Identify words that appear in all documents
        Map<String, Integer> wordFrequencyMap = computeWordFrequency(emails);
        List<String> wordsAppearingInAll = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : wordFrequencyMap.entrySet()) {
            if (entry.getValue() == emails.size()) {
                wordsAppearingInAll.add(entry.getKey());
            }
        }

        return wordsAppearingInAll;
    }

    public List<String> extractUniqueWords(List<String> emails) {
        // Identify words that appear in different fractions of documents
        Map<String, Integer> wordFrequencyMap = computeWordFrequency(emails);
        List<String> wordsAppearingInSome = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : wordFrequencyMap.entrySet()) {
            if (entry.getValue() < emails.size()) {
                wordsAppearingInSome.add(entry.getKey());
            }
        }

        return wordsAppearingInSome;
    }

    public void save_and_display_SummaryToCSV(List<String> emails, String outputDir) {
        List<Integer> wordCountsList = new ArrayList<>();

        for (String email : emails) {
            wordCountsList.add(extractWordCount(email));
        }

        try {
            FileWriter writer = new FileWriter(outputDir);
            writer.append("Statistic,Value\n");
            writer.append("Word Counts Min," + min(wordCountsList) + "\n");
            writer.append("Word Counts Max," + max(wordCountsList) + "\n");
            writer.append("Word Counts Median," + calculateMedian(wordCountsList) + "\n");
            writer.append("Word Counts Mean," + calculateMean(wordCountsList) + "\n");
            writer.append("Word Counts Std Dev," + calculateStandardDeviation(wordCountsList) + "\n");

            System.out.println("Word Counts - Min," + min(wordCountsList));
            System.out.println("Word Counts - Max," + max(wordCountsList));
            System.out.println("Word Counts - Median," + calculateMedian(wordCountsList));
            System.out.println("Word Counts - Mean," + calculateMean(wordCountsList));
            System.out.println("Word Counts - Std Dev," + calculateStandardDeviation(wordCountsList));

            List<String> commonWords = extractCommonWords(emails);
            List<String> uniqueWords = extractUniqueWords(emails);

            writer.append("Words Appearing in All Documents," + String.join("-", commonWords) + "\n");
            System.out.println("Words Appearing in All Documents: " + commonWords);
            writer.append("Words Appearing in Some Documents," + String.join("-", uniqueWords) + "\n");
            System.out.println("Words Appearing in Some Documents: " + uniqueWords);
            writer.close();
            System.out.println("FEATURE SUMMARY SAVED SUCCESSFULLY IN : " + outputDir);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
