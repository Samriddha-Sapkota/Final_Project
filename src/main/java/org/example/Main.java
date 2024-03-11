package org.example;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String dbUrl = "jdbc:sqlite:teacher.db";
        createTeacherTable(dbUrl);

        boolean exit = false;
        while (!exit) {
            System.out.println("\nWelcome to Teacher Management System!");
            System.out.println("======================================");
            System.out.println("Please select an option:");
            System.out.println("1. Add Teacher");
            System.out.println("2. View Teachers");
            System.out.println("3. Delete Teacher");
            System.out.println("4. Update Teacher");
            System.out.println("5. Exit");
            System.out.println("======================================");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    addTeacher(scanner, dbUrl);
                    break;
                case 2:
                    viewTeachers(dbUrl);
                    break;
                case 3:
                    deleteTeacher(scanner, dbUrl);
                    break;
                case 4:
                    updateTeacher(scanner, dbUrl);
                    break;
                case 5:
                    exit = true;
                    System.out.println("Exiting the program...");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private static void createTeacherTable(String dbUrl) {
        String createTable = "CREATE TABLE IF NOT EXISTS teacher (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, subject TEXT)";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTable);
            System.out.println("Table created successfully");
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    private static void addTeacher(Scanner scanner, String dbUrl) {
        System.out.println("\nEnter teacher details:");
        System.out.print("Enter teacher name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter teacher subject: ");
        String subject = scanner.nextLine().trim();

        String insertQuery = "INSERT INTO teacher (name, subject) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            pstmt.setString(1, name);
            pstmt.setString(2, subject);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("\nTeacher added successfully");
            } else {
                System.out.println("\nFailed to add teacher");
            }
        } catch (SQLException e) {
            System.out.println("Error adding teacher: " + e.getMessage());
        }
    }

    private static void viewTeachers(String dbUrl) {
        String selectQuery = "SELECT * FROM teacher";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectQuery)) {
            System.out.println("\nTeacher details:");
            System.out.println("ID\tName\tSubject");
            System.out.println("----------------------------------");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "\t" + rs.getString("name") + "\t" + rs.getString("subject"));
            }
        } catch (SQLException e) {
            System.out.println("Error viewing teachers: " + e.getMessage());
        }
    }

    private static void deleteTeacher(Scanner scanner, String dbUrl) {
        System.out.print("\nEnter teacher ID to delete: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Please enter a number.");
            return;
        }

        String deleteQuery = "DELETE FROM teacher WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("\nTeacher deleted successfully");
            } else {
                System.out.println("\nNo teacher found with the provided ID");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting teacher: " + e.getMessage());
        }
    }

    private static void updateTeacher(Scanner scanner, String dbUrl) {
        System.out.print("\nEnter teacher ID to update: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Please enter a number.");
            return;
        }

        System.out.print("Enter new teacher name: ");
        String newName = scanner.nextLine().trim();
        System.out.print("Enter new teacher subject: ");
        String newSubject = scanner.nextLine().trim();

        String updateQuery = "UPDATE teacher SET name = ?, subject = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setString(1, newName);
            pstmt.setString(2, newSubject);
            pstmt.setInt(3, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("\nTeacher updated successfully");
            } else {
                System.out.println("\nNo teacher found with the provided ID");
            }
        } catch (SQLException e) {
            System.out.println("Error updating teacher: " + e.getMessage());
        }
    }
}
