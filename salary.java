import java.util.Locale;
import java.util.Scanner;

class SalaryCalculator {

    // Method to calculate hourly rate based on annual salary and weekly working hours
    public double calculateHourlyRate(double annualSalary, int weeklyHours) {
        int totalWorkingWeeks = 52; // Assuming a year has 52 weeks
        return annualSalary / (weeklyHours * totalWorkingWeeks);
    }

    // Method to calculate daily earnings based on hourly rate and daily working hours
    public double calculateDailyEarnings(double hourlyRate, int dailyHours) {
        return hourlyRate * dailyHours;
    }

    // Method to calculate weekly earnings based on hourly rate and weekly working hours
    public double calculateWeeklyEarnings(double hourlyRate, int weeklyHours) {
        return hourlyRate * weeklyHours;
    }

    // Method to calculate annual earnings based on hourly rate and weekly working hours
    public double calculateAnnualEarnings(double hourlyRate, int weeklyHours) {
        int totalWorkingWeeks = 52;
        return calculateWeeklyEarnings(hourlyRate, weeklyHours) * totalWorkingWeeks;
    }

    // Method to calculate tax based on the German tax system for 2024
    public double calculateTax(double annualSalary) {
        double tax = 0;

        // German progressive tax rates for 2024
        if (annualSalary <= 11604) {
            tax = 0; // No tax for income up to $11,604
        } else if (annualSalary <= 66760) {
            // Tax rates range from 14% to 42% between $11,605 and $66,760
            tax = annualSalary * 0.14; // Applying minimum 14% tax for the range
        } else if (annualSalary <= 277825) {
            // 42% tax rate for income between $66,761 and $277,825
            tax = annualSalary * 0.42;
        } else {
            // 45% tax rate for income above $277,825
            tax = annualSalary * 0.45;
        }

        // Add the solidarity surcharge (5.5% on the income tax)
        tax += tax * 0.055;

        return tax;
    }
}

class ConsoleUI {
    private Scanner scanner;

    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
    }

    // Method to prompt user for monthly salary
    public double promptForMonthlySalary() {
        System.out.print("Enter your monthly salary: ");
        return validatePositiveDoubleInput();
    }

    // Method to prompt user for the number of working hours per day
    public int promptForDailyHours() {
        System.out.print("Enter the number of hours you work per day: ");
        return validatePositiveIntInput();
    }

    // Method to display the calculation results
    public void displayResults(double hourlyRate, double dailyEarnings, double weeklyEarnings, double annualEarnings, double tax, int totalMonthlyHours) {
        System.out.println("\n---- Earnings Breakdown ----");
        System.out.printf("Hourly Rate: $ %.2f\n", hourlyRate);
        System.out.printf("Daily Earnings: $ %.2f\n", dailyEarnings);
        System.out.printf("Weekly Earnings: $ %.2f\n", weeklyEarnings);
        System.out.printf("Annual Earnings: $ %.2f\n", annualEarnings);
        
        System.out.println("\n---- Tax and Net Earnings ----");
        System.out.printf("Tax Deducted: $ %.2f\n", tax);
        System.out.printf("Net Annual Earnings: $ %.2f\n", annualEarnings - tax);
        
        System.out.println("\n---- Monthly Breakdown ----");
        System.out.printf("Total Hours Worked per Month: %d hours\n", totalMonthlyHours);
    }

    // Helper method to validate positive double inputs
    private double validatePositiveDoubleInput() {
        while (!scanner.hasNextDouble()) {
            System.out.print("Invalid input. Please enter a valid number: ");
            scanner.next();
        }
        double value = scanner.nextDouble();
        while (value < 0) {
            System.out.print("Invalid input. Value must be positive: ");
            value = scanner.nextDouble();
        }
        return value;
    }

    // Helper method to validate positive integer inputs
    private int validatePositiveIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a valid integer: ");
            scanner.next();
        }
        int value = scanner.nextInt();
        while (value <= 0) {
            System.out.print("Invalid input. Value must be greater than zero: ");
            value = scanner.nextInt();
        }
        return value;
    }

    // Method to close scanner when done
    public void closeScanner() {
        scanner.close();
    }
}

public class Main {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        ConsoleUI ui = new ConsoleUI();
        SalaryCalculator calculator = new SalaryCalculator();

        try {
            System.out.println("Germany Salary Calculator");

            double monthlySalary = ui.promptForMonthlySalary();
            int dailyHours = ui.promptForDailyHours();

            // Calculate the equivalent annual salary
            double annualSalary = monthlySalary * 12;

            // Assume a standard 40-hour workweek for simplicity
            int weeklyHours = 40;

            // Perform calculations
            double hourlyRate = calculator.calculateHourlyRate(annualSalary, weeklyHours);
            double dailyEarnings = calculator.calculateDailyEarnings(hourlyRate, dailyHours);
            double weeklyEarnings = calculator.calculateWeeklyEarnings(hourlyRate, weeklyHours);
            double annualEarnings = calculator.calculateAnnualEarnings(hourlyRate, weeklyHours);
            double tax = calculator.calculateTax(annualSalary);

            // Calculate total hours worked in the month
            int totalMonthlyHours = dailyHours * 22;  // Assuming 22 working days per month

            // Display the results
            ui.displayResults(hourlyRate, dailyEarnings, weeklyEarnings, annualEarnings, tax, totalMonthlyHours);
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            ui.closeScanner();
        }
    }
}
