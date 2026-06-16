package com.sdkfz.procedures;

import net.minecraft.world.entity.Entity;

public class FuelGenerationProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		entity.getPersistentData().putDouble("FuelCapacity", 24);
		entity.getPersistentData().putDouble("FuelEfficiency", 0.005);
		entity.getPersistentData().putDouble("Fuel", 0);
		entity.getPersistentData().putDouble("MaxVehicleGear", 5);
		entity.getPersistentData().putDouble("MinVehicleGear", (-1));
		entity.getPersistentData().putDouble("VehicleGear", 0);
	}
}
