package assignment;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.List;

public class EmailFeatureVisualizer {
    
    public void createAndShowGUI() {
        JFrame frame = new JFrame("Email Feature Summary");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel(new GridLayout(2, 1));

        // Create a text area to display summary
        JTextArea summaryTextArea = new JTextArea();
        summaryTextArea.setEditable(false);
        JScrollPane summaryScrollPane = new JScrollPane(summaryTextArea);

        // Create a text area to display common and unique words
        JTextArea wordsTextArea = new JTextArea();
        wordsTextArea.setEditable(false);
        JScrollPane wordsScrollPane = new JScrollPane(wordsTextArea);

        panel.add(summaryScrollPane);
        panel.add(wordsScrollPane);

        frame.add(panel);
        frame.setVisible(true);

        // Load summary data from the CSV
        String summaryCsvPath = "src/summary_of_features.csv"; // Update with the correct path
        loadSummaryFromCSV(summaryCsvPath, summaryTextArea, wordsTextArea);
    }

    public void loadSummaryFromCSV(String csvPath, JTextArea summaryTextArea, JTextArea wordsTextArea) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(csvPath));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String key = parts[0];
                    String value = parts[1];
                    if (key.startsWith("Words Appearing in")) {
                        wordsTextArea.append(value + "\n");
                    } else {
                        summaryTextArea.append(key + ": " + value + "\n");
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
