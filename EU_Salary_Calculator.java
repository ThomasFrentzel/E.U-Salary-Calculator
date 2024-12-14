import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EU_Salary_Calculator {
    public static void main(String[] args) {
        // Create and display the GUI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SalaryCalculatorGUI();
            }
        });
    }
}

class SalaryCalculatorGUI extends JFrame {
    private JTextField salaryField;
    private JTextField dailyHoursField;
    private JTextField companyField;  // New field for company name
    private JPanel resultPanel;
    private JButton downloadButton;

    // Variables to store the results
    private double hourlyRate, dailyEarnings, weeklyEarnings, annualEarnings, tax, netAnnualEarnings;
    private String companyName;  // Variable to store company name

    public SalaryCalculatorGUI() {
        setTitle("Salary Calculator");
        setSize(500, 450);  // Adjusted size to fit company name field
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Create components
        JLabel salaryLabel = new JLabel("Enter your monthly salary:");
        salaryField = new JTextField(10);
        JLabel dailyHoursLabel = new JLabel("Enter the number of hours you work per day:");
        dailyHoursField = new JTextField(10);
        JLabel companyLabel = new JLabel("Enter the company name:");  // Label for company name
        companyField = new JTextField(10);  // Input field for company name
        JButton calculateButton = new JButton("Calculate");

        // Create a panel to hold the results and group them neatly
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        JScrollPane resultScrollPane = new JScrollPane(resultPanel);
        resultScrollPane.setPreferredSize(new Dimension(400, 200));  // Set preferred size for the result scroll pane

        // Add components to the frame
        setLayout(new FlowLayout());
        add(salaryLabel);
        add(salaryField);
        add(dailyHoursLabel);
        add(dailyHoursField);
        add(companyLabel);
        add(companyField);
        add(calculateButton);
        add(resultScrollPane);

        // Create and add download button
        downloadButton = new JButton("Download as .txt");
        add(downloadButton);

        // Set button actions
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double monthlySalary = Double.parseDouble(salaryField.getText().replace(",", "."));
                    int dailyHours = Integer.parseInt(dailyHoursField.getText());
                    companyName = companyField.getText();  // Get company name

                    // Assume a standard 40-hour workweek
                    int weeklyHours = 40;
                    double annualSalary = monthlySalary * 12;
                    hourlyRate = annualSalary / (weeklyHours * 52);
                    dailyEarnings = hourlyRate * dailyHours;
                    weeklyEarnings = hourlyRate * weeklyHours;
                    annualEarnings = hourlyRate * weeklyHours * 52;

                    // Tax calculation (simplified for illustration)
                    tax = calculateTax(annualSalary);
                    netAnnualEarnings = annualEarnings - tax;

                    // Display results with 2 decimal places
                    resultPanel.removeAll();  // Clear previous results
                    resultPanel.add(createResultPanel("Company Name: ", companyName));  // Display company name
                    resultPanel.add(createResultPanel("Monthly Salary: €", String.format("%.2f", monthlySalary)));
                    resultPanel.add(createResultPanel("Hours per Day: ", dailyHours));
                    resultPanel.add(createResultPanel("Hourly Rate: €", String.format("%.2f", hourlyRate)));
                    resultPanel.add(createResultPanel("Daily Earnings: €", String.format("%.2f", dailyEarnings)));
                    resultPanel.add(createResultPanel("Weekly Earnings: €", String.format("%.2f", weeklyEarnings)));
                    resultPanel.add(createResultPanel("Annual Earnings: €", String.format("%.2f", annualEarnings)));
                    resultPanel.add(createResultPanel("Tax Deducted: €", String.format("%.2f", tax)));
                    resultPanel.add(createResultPanel("Net Annual Earnings: €", String.format("%.2f", netAnnualEarnings)));

                    resultPanel.revalidate();  // Revalidate to update UI
                    resultPanel.repaint();  // Refresh the panel to show the results

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(SalaryCalculatorGUI.this, "Please enter valid numbers.",
                            "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Download button action
        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Generate the text content to be saved
                    StringBuilder sb = new StringBuilder();
                    sb.append("Salary Calculation Results:\n\n");

                    // General Information Section
                    sb.append("=== General Information ===\n");
                    sb.append("Company Name: " + companyName + "\n");
                    sb.append("Monthly Salary: €" + String.format("%.2f", Double.parseDouble(salaryField.getText().replace(",", "."))) + "\n");
                    sb.append("Hours per Day: " + dailyHoursField.getText() + "\n\n");

                    // Salary Breakdown Section
                    sb.append("=== Salary Breakdown ===\n");
                    sb.append("Hourly Rate: €" + String.format("%.2f", hourlyRate) + "\n");
                    sb.append("Daily Earnings: €" + String.format("%.2f", dailyEarnings) + "\n");
                    sb.append("Weekly Earnings: €" + String.format("%.2f", weeklyEarnings) + "\n");
                    sb.append("Annual Earnings: €" + String.format("%.2f", annualEarnings) + "\n\n");

                    // Tax Calculation Section
                    sb.append("=== Tax Calculation ===\n");
                    sb.append("Tax Deducted: €" + String.format("%.2f", tax) + "\n");
                    sb.append("Net Annual Earnings: €" + String.format("%.2f", netAnnualEarnings) + "\n");

                    // Ask the user to choose the location and name of the file
                    JFileChooser fileChooser = new JFileChooser();
                    int result = fileChooser.showSaveDialog(SalaryCalculatorGUI.this);

                    if (result == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        if (file != null) {
                            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                            writer.write(sb.toString());
                            writer.close();
                            JOptionPane.showMessageDialog(SalaryCalculatorGUI.this, "File saved successfully.");
                        }
                    }

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(SalaryCalculatorGUI.this, "Error saving file.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setVisible(true);
    }

    // Helper method to create a nicely formatted result panel
    private JPanel createResultPanel(String label, Object value) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));  // Add padding for neatness

        // Create a label to display the value with some formatting
        JLabel resultLabel = new JLabel(label + value);
        resultLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        resultLabel.setPreferredSize(new Dimension(300, 30));  // Set a fixed size for each label

        panel.add(resultLabel);
        return panel;
    }

    // Tax calculation method (simplified)
    private double calculateTax(double annualSalary) {
        double tax = 0;

        if (annualSalary <= 11604) {
            tax = 0; // No tax for income up to €11,604
        } else if (annualSalary <= 66760) {
            tax = annualSalary * 0.14; // Minimum 14% tax for the range
        } else if (annualSalary <= 277825) {
            tax = annualSalary * 0.42; // 42% tax rate
        } else {
            tax = annualSalary * 0.45; // 45% tax rate
        }

        // Add the solidarity surcharge (5.5% on the income tax)
        tax += tax * 0.055;

        return tax;
    }
}
