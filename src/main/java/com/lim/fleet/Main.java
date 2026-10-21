package com.lim.fleet;

import com.lim.fleet.dao.DatabaseConnection;
import com.lim.fleet.ui.VehicleMenu;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            DatabaseConnection.initialize();
        } catch (SQLException e) {
            System.out.println("Failed to initialize database: " + e.getMessage());
            return;
        }
        new VehicleMenu().run();
    }
}
