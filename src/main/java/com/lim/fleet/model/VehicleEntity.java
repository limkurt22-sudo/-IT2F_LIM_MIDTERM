package com.lim.fleet.model;

/**
 * Immutable vehicle record (5 attributes max).
 * For a new vehicle, pass id = 0; the database assigns the real ID (starting at 1).
 */
public record VehicleEntity(int id, String modelName, String plateNo, double dailyRate, String status) {
}
