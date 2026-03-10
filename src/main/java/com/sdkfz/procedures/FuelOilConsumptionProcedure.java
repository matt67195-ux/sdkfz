package com.sdkfz.procedures;

import net.minecraft.world.entity.Entity;

public class FuelOilConsumptionProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		double fuelEfficiency = 0;
		double fuel = 0;
		double fuelDistanceOffset = 0;
		String nbtVehicleGear = "";
		String nbtFuelEfficiency = "";
		String nbtFuel = "";
		fuelDistanceOffset = 1;
		nbtFuel = "Fuel";
		nbtFuelEfficiency = "FuelEfficiency";
		nbtVehicleGear = "VehicleGear";
		if (entity.isVehicle() && entity.getPersistentData().getDouble(nbtFuel) > 0 && (entity.getPersistentData().getDouble(nbtVehicleGear) < 0 || entity.getPersistentData().getDouble(nbtVehicleGear) > 0)) {
			if (entity.getDeltaMovement().x() != 0 || entity.getDeltaMovement().z() != 0) {
				fuel = entity.getPersistentData().getDouble(nbtFuel);
				fuelEfficiency = entity.getPersistentData().getDouble(nbtFuelEfficiency);
				if ((Math.abs(entity.getDeltaMovement().x()) + Math.abs(entity.getDeltaMovement().z())) / 2 >= 1) {
					entity.getPersistentData().putDouble(nbtFuel, (fuel - (fuelEfficiency * ((Math.abs(entity.getDeltaMovement().x()) + Math.abs(entity.getDeltaMovement().z())) / 2)) / fuelDistanceOffset));
				} else {
					entity.getPersistentData().putDouble(nbtFuel, (fuel - (fuelEfficiency / ((Math.abs(entity.getDeltaMovement().x()) + Math.abs(entity.getDeltaMovement().z())) / 2 + 1)) / fuelDistanceOffset));
				}
				if (entity.getPersistentData().getDouble(nbtFuel) < 0) {
					entity.getPersistentData().putDouble(nbtFuel, 0);
				}
			}
		}
	}
}
