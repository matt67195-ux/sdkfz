package com.sdkfz.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;

public class VehicleShiftProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		String nbtVehicleGear = "";
		nbtVehicleGear = "VehicleGear";
		if (entity.getPersistentData().getDouble(nbtVehicleGear) == 0) {
			entity.setDeltaMovement(new Vec3(0, (entity.getDeltaMovement().y()), 0));
		} else if (entity.getPersistentData().getDouble(nbtVehicleGear) == 1) {
			entity.setDeltaMovement(new Vec3((entity.getDeltaMovement().x() + entity.getLookAngle().x / 6), (entity.getDeltaMovement().y()), (entity.getDeltaMovement().z() + entity.getLookAngle().z / 6)));
		} else if (entity.getPersistentData().getDouble(nbtVehicleGear) == 2) {
			entity.setDeltaMovement(new Vec3((entity.getDeltaMovement().x() + entity.getLookAngle().x / 5.5), (entity.getDeltaMovement().y()), (entity.getDeltaMovement().z() + entity.getLookAngle().z / 5.5)));
		} else if (entity.getPersistentData().getDouble(nbtVehicleGear) == 3) {
			entity.setDeltaMovement(new Vec3((entity.getDeltaMovement().x() + entity.getLookAngle().x / 5), (entity.getDeltaMovement().y()), (entity.getDeltaMovement().z() + entity.getLookAngle().z / 5)));
		} else if (entity.getPersistentData().getDouble(nbtVehicleGear) == 4) {
			entity.setDeltaMovement(new Vec3((entity.getDeltaMovement().x() + entity.getLookAngle().x / 4.5), (entity.getDeltaMovement().y()), (entity.getDeltaMovement().z() + entity.getLookAngle().z / 4.5)));
		} else if (entity.getPersistentData().getDouble(nbtVehicleGear) == 5) {
			entity.setDeltaMovement(new Vec3((entity.getDeltaMovement().x() + entity.getLookAngle().x / 4), (entity.getDeltaMovement().y()), (entity.getDeltaMovement().z() + entity.getLookAngle().z / 4)));
		} else if (entity.getPersistentData().getDouble(nbtVehicleGear) == -1) {
			entity.setDeltaMovement(new Vec3((entity.getDeltaMovement().x() - entity.getLookAngle().x / 6), (entity.getDeltaMovement().y()), (entity.getDeltaMovement().z() - entity.getLookAngle().z / 6)));
		}
	}
}
