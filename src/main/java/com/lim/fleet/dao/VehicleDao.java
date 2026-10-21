package com.lim.fleet.dao;

import com.lim.fleet.model.VehicleEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Data access layer: parameterized SQL only (PreparedStatement). */
public class VehicleDao {

    private static final String INSERT_SQL =
            "INSERT INTO vehicles (MODEL_NAME, PLATE_NO, DAILY_RATE, STATUS) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_SQL =
            "SELECT ID, MODEL_NAME, PLATE_NO, DAILY_RATE, STATUS FROM vehicles ORDER BY ID";
    private static final String EXISTS_PLATE_SQL =
            "SELECT 1 FROM vehicles WHERE PLATE_NO = ?";

    public void insert(VehicleEntity v) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {
            ps.setString(1, v.modelName());
            ps.setString(2, v.plateNo());
            ps.setDouble(3, v.dailyRate());
            ps.setString(4, v.status());
            ps.executeUpdate();
        }
    }

    public List<VehicleEntity> findAll() throws SQLException {
        List<VehicleEntity> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new VehicleEntity(
                        rs.getInt("ID"),
                        rs.getString("MODEL_NAME"),
                        rs.getString("PLATE_NO"),
                        rs.getDouble("DAILY_RATE"),
                        rs.getString("STATUS")));
            }
        }
        return list;
    }

    public boolean plateExists(String plateNo) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(EXISTS_PLATE_SQL)) {
            ps.setString(1, plateNo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
