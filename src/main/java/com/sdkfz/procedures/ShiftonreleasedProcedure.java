package com.sdkfz.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

import com.sdkfz.network.SdkfzModVariables;
import com.sdkfz.SdkfzMod;

public class ShiftonreleasedProcedure {
	public static void execute(LevelAccessor world, Entity entity, double pressedms) {
		if (entity == null)
			return;
		String nbtVehicleGear = "";
		String nbtMaxVehicleGear = "";
		String nbtMinVehicleGear = "";
		Entity vehicleEntity = null;
		if (entity.isPassenger()) {
			vehicleEntity = entity.getVehicle();
			if (vehicleEntity.getType().is(TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("forge:vehicle/car")))) {
				nbtVehicleGear = "VehicleGear";
				nbtMaxVehicleGear = "MaxVehicleGear";
				nbtMinVehicleGear = "MinVehicleGear";
				{
					boolean _setval = true;
					entity.getCapability(SdkfzModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
						capability.KeyReleased = _setval;
						capability.syncPlayerVariables(entity);
					});
				}
				if (pressedms >= 500) {
					if (vehicleEntity.getPersistentData().getDouble(nbtVehicleGear) > vehicleEntity.getPersistentData().getDouble(nbtMinVehicleGear)) {
						vehicleEntity.getPersistentData().putDouble(nbtVehicleGear, (vehicleEntity.getPersistentData().getDouble(nbtVehicleGear) - 1));
						{
							String _setval = "Now in gear: " + vehicleEntity.getPersistentData().getDouble(nbtVehicleGear);
							entity.getCapability(SdkfzModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.KeyReleasedMessage = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal(((entity.getCapability(SdkfzModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new SdkfzModVariables.PlayerVariables())).KeyReleasedMessage)), true);
					} else {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal(""), true);
					}
				} else {
					if (vehicleEntity.getPersistentData().getDouble(nbtVehicleGear) < vehicleEntity.getPersistentData().getDouble(nbtMaxVehicleGear)) {
						vehicleEntity.getPersistentData().putDouble(nbtVehicleGear, (vehicleEntity.getPersistentData().getDouble(nbtVehicleGear) + 1));
						{
							String _setval = "Now in gear: " + vehicleEntity.getPersistentData().getDouble(nbtVehicleGear);
							entity.getCapability(SdkfzModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.KeyReleasedMessage = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
						SdkfzMod.queueServerWork(2, () -> {
							if (entity instanceof Player _player && !_player.level().isClientSide())
								_player.displayClientMessage(Component.literal(((entity.getCapability(SdkfzModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new SdkfzModVariables.PlayerVariables())).KeyReleasedMessage)), true);
						});
					} else {
						SdkfzMod.queueServerWork(2, () -> {
							if (entity instanceof Player _player && !_player.level().isClientSide())
								_player.displayClientMessage(Component.literal(""), true);
						});
					}
				}
			}
		}
	}
}
