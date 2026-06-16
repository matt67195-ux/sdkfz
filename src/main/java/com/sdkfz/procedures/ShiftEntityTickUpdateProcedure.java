package com.sdkfz.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;

public class ShiftEntityTickUpdateProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		VehicleShiftProcedure.execute(entity);
		FuelOilConsumptionProcedure.execute(entity);
		VehicleFallAndStopProcedure.execute(world, entity);
	}
}
