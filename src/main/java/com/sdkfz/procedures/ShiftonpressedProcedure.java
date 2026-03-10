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

public class ShiftonpressedProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		Entity vehicleEntity = null;
		if (entity.isPassenger()) {
			vehicleEntity = entity.getVehicle();
			if (vehicleEntity.getType().is(TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("forge:vehicle/car")))) {
				{
					boolean _setval = false;
					entity.getCapability(SdkfzModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
						capability.KeyReleased = _setval;
						capability.syncPlayerVariables(entity);
					});
				}
				SdkfzMod.queueServerWork(1, () -> {
					if (entity instanceof Player _player && !_player.level().isClientSide())
						_player.displayClientMessage(Component.literal("Shifting: \u2588\u2593\u2592\u2591\u2591\u2591\u2591\u2591\u2591\u2591 \u25B2"), true);
					if (!(entity.getCapability(SdkfzModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new SdkfzModVariables.PlayerVariables())).KeyReleased) {
						SdkfzMod.queueServerWork(1, () -> {
							if (entity instanceof Player _player && !_player.level().isClientSide())
								_player.displayClientMessage(Component.literal("Shifting: \u2588\u2588\u2593\u2592\u2591\u2591\u2591\u2591\u2591\u2591 \u25B2"), true);
							if (!(entity.getCapability(SdkfzModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new SdkfzModVariables.PlayerVariables())).KeyReleased) {
								SdkfzMod.queueServerWork(1, () -> {
									if (entity instanceof Player _player && !_player.level().isClientSide())
										_player.displayClientMessage(Component.literal("Shifting: \u2588\u2588\u2588\u2593\u2592\u2591\u2591\u2591\u2591\u2591 \u25B2"), true);
									if (!(entity.getCapability(SdkfzModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new SdkfzModVariables.PlayerVariables())).KeyReleased) {
										SdkfzMod.queueServerWork(1, () -> {
											if (entity instanceof Player _player && !_player.level().isClientSide())
												_player.displayClientMessage(Component.literal("Shifting: \u2588\u2588\u2588\u2588\u2593\u2592\u2591\u2591\u2591\u2591 \u25B2"), true);
											if (!(entity.getCapability(SdkfzModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new SdkfzModVariables.PlayerVariables())).KeyReleased) {
												SdkfzMod.queueServerWork(1, () -> {
													if (entity instanceof Player _player && !_player.level().isClientSide())
														_player.displayClientMessage(Component.literal("Shifting: \u2588\u2588\u2588\u2588\u2588\u2593\u2592\u2591\u2591\u2591 \u25B2"), true);
													if (!(entity.getCapability(SdkfzModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new SdkfzModVariables.PlayerVariables())).KeyReleased) {
														SdkfzMod.queueServerWork(1, () -> {
															if (entity instanceof Player _player && !_player.level().isClientSide())
																_player.displayClientMessage(Component.literal("Shifting: \u2588\u2588\u2588\u2588\u2588\u2588\u2593\u2592\u2591\u2591 \u25B2"), true);
															if (!(entity.getCapability(SdkfzModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new SdkfzModVariables.PlayerVariables())).KeyReleased) {
																SdkfzMod.queueServerWork(1, () -> {
																	if (entity instanceof Player _player && !_player.level().isClientSide())
																		_player.displayClientMessage(Component.literal("Shifting: \u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2593\u2592\u2591 \u25B2"), true);
																	if (!(entity.getCapability(SdkfzModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new SdkfzModVariables.PlayerVariables())).KeyReleased) {
																		SdkfzMod.queueServerWork(1, () -> {
																			if (entity instanceof Player _player && !_player.level().isClientSide())
																				_player.displayClientMessage(Component.literal("Shifting: \u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2593\u2592 \u25B2"), true);
																			if (!(entity.getCapability(SdkfzModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new SdkfzModVariables.PlayerVariables())).KeyReleased) {
																				SdkfzMod.queueServerWork(1, () -> {
																					if (entity instanceof Player _player && !_player.level().isClientSide())
																						_player.displayClientMessage(Component.literal("Shifting: \u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2593 \u25B2"), true);
																					if (!(entity.getCapability(SdkfzModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new SdkfzModVariables.PlayerVariables())).KeyReleased) {
																						SdkfzMod.queueServerWork(1, () -> {
																							if (entity instanceof Player _player && !_player.level().isClientSide())
																								_player.displayClientMessage(Component.literal("Shifting: \u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588 \u25BC"), true);
																						});
																					}
																				});
																			}
																		});
																	}
																});
															}
														});
													}
												});
											}
										});
									}
								});
							}
						});
					}
				});
			}
		}
	}
}
