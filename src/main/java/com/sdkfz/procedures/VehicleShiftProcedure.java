package com.sdkfz.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class VehicleShiftProcedure {
    public static void execute(Entity vehicle) {
        if (vehicle == null) return;

        Entity driver = null;
        if (!vehicle.getPassengers().isEmpty()) {
            driver = vehicle.getPassengers().get(0);
        }

        // Bug fix: ignorar conductor muerto o inválido y resetear estado
        if (driver == null || !driver.isAlive()) {
            if (driver != null) {
                // Forzar expulsión del mob muerto que quedó atrapado como passenger
                driver.stopRiding();
            }
            vehicle.getPersistentData().putDouble("VehicleGear", 0);
            vehicle.setDeltaMovement(new Vec3(0, vehicle.getDeltaMovement().y(), 0));
            return;
        }

        Vec3 lookAngle = driver.getLookAngle();
        double gear = vehicle.getPersistentData().getDouble("VehicleGear");

        System.out.println("VehicleShiftProcedure ejecutado, marcha: " + gear + ", conductor: " + driver);

        if (gear == 0) {
            vehicle.setDeltaMovement(new Vec3(0, vehicle.getDeltaMovement().y(), 0));
        } else if (gear == 1) {
            vehicle.setDeltaMovement(new Vec3(vehicle.getDeltaMovement().x() + lookAngle.x / 6, vehicle.getDeltaMovement().y(), vehicle.getDeltaMovement().z() + lookAngle.z / 6));
        } else if (gear == 2) {
            vehicle.setDeltaMovement(new Vec3(vehicle.getDeltaMovement().x() + lookAngle.x / 5.5, vehicle.getDeltaMovement().y(), vehicle.getDeltaMovement().z() + lookAngle.z / 5.5));
        } else if (gear == 3) {
            vehicle.setDeltaMovement(new Vec3(vehicle.getDeltaMovement().x() + lookAngle.x / 5, vehicle.getDeltaMovement().y(), vehicle.getDeltaMovement().z() + lookAngle.z / 5));
        } else if (gear == 4) {
            vehicle.setDeltaMovement(new Vec3(vehicle.getDeltaMovement().x() + lookAngle.x / 4.5, vehicle.getDeltaMovement().y(), vehicle.getDeltaMovement().z() + lookAngle.z / 4.5));
        } else if (gear == 5) {
            vehicle.setDeltaMovement(new Vec3(vehicle.getDeltaMovement().x() + lookAngle.x / 4, vehicle.getDeltaMovement().y(), vehicle.getDeltaMovement().z() + lookAngle.z / 4));
        } else if (gear == -1) {
            vehicle.setDeltaMovement(new Vec3(vehicle.getDeltaMovement().x() - lookAngle.x / 6, vehicle.getDeltaMovement().y(), vehicle.getDeltaMovement().z() - lookAngle.z / 6));
        }
    }
}
