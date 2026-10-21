package com.lim.fleet.ui;

import com.lim.fleet.dao.VehicleDao;
import com.lim.fleet.model.VehicleEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class VehicleMenu {

    private static final String LINE = "=".repeat(52);
    private static final String WIDE_LINE = "=".repeat(80);

    private final Scanner scanner = new Scanner(System.in);
    private final VehicleDao dao = new VehicleDao();

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1 -> addVehicle();
                case 2 -> viewFleet();
                case 3 -> {
                    System.out.println("\nExiting system... Thank you!");
                    running = false;
                }
                default -> System.out.println("\nERROR: Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n" + LINE);
        System.out.println("            CAR RENTAL FLEET TRACKER");
        System.out.println(LINE);
        System.out.println("[1] Add New Vehicle");
        System.out.println("[2] View Fleet Vehicles");
        System.out.println("[3] Exit System");
        System.out.println(LINE);
    }

    private void addVehicle() {
        System.out.println("\n--- ADD NEW VEHICLE ---");
        String model = readNonEmpty("Enter Model Name: ");

        String plate;
        while (true) {
            plate = readNonEmpty("Enter License Plate Number: ").toUpperCase();
            try {
                if (dao.plateExists(plate)) {
                    System.out.println("ERROR: Plate number already registered. Try again.");
                    continue;
                }
            } catch (SQLException e) {
                System.out.println("DATABASE ERROR: " + e.getMessage());
                return;
            }
            break;
        }

        double rate;
        while (true) {
            rate = readDouble("Enter Daily Rate: ");
            if (rate > 0) break;
            System.out.println("ERROR: Daily rate must be greater than 0.");
        }

        String status;
        while (true) {
            status = readNonEmpty("Enter Status (AVAILABLE / RENTED): ").toUpperCase();
            if (status.equals("AVAILABLE") || status.equals("RENTED")) break;
            System.out.println("ERROR: Status must be AVAILABLE or RENTED.");
        }

        try {
            dao.insert(new VehicleEntity(0, model, plate, rate, status));
            System.out.println("\nSUCCESS: Vehicle registered to fleet!");
        } catch (SQLException e) {
            System.out.println("DATABASE ERROR: " + e.getMessage());
        }
    }

    private void viewFleet() {
        List<VehicleEntity> fleet;
        try {
            fleet = dao.findAll();
        } catch (SQLException e) {
            System.out.println("DATABASE ERROR: " + e.getMessage());
            return;
        }

        System.out.println("\n" + WIDE_LINE);
        System.out.println("                              RENTAL FLEET VEHICLES");
        System.out.println(WIDE_LINE);

        if (fleet.isEmpty()) {
            System.out.println("No vehicles registered yet.");
            return;
        }

        String border = "+----+--------------------+------------+--------------+----------------+";
        System.out.println(border);
        System.out.printf("| %-2s | %-18s | %-10s | %-12s | %-14s |%n",
                "ID", "MODEL NAME", "PLATE NO", "DAILY RATE", "STATUS");
        System.out.println(border);
        for (VehicleEntity v : fleet) {
            System.out.printf("| %-2d | %-18s | %-10s | %-12.2f | %-14s |%n",
                    v.id(), v.modelName(), v.plateNo(), v.dailyRate(), v.status());
        }
        System.out.println(border);
        System.out.println("Total Records Displayed: " + fleet.size());
    }

    // ---- Input helpers: always read whole lines, so no leftover newline tokens ----

    private String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) return line;
            System.out.println("ERROR: Input cannot be empty.");
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Please enter a whole number.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Please enter a valid number (e.g., 2000.00).");
            }
        }
    }
}
