package com.sdkfz.procedures;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.tags.BlockTags;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

public class VehicleFallAndStopProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		double block_check_distance = 0;
		double drop_force = 0;
		String nbtVehicleGear = "";
		String nbtFuel = "";
		String tagVehicleSolidBlocks = "";
		drop_force = 1;
		block_check_distance = 0.0625;
		nbtFuel = "Fuel";
		nbtVehicleGear = "VehicleGear";
		tagVehicleSolidBlocks = "forge:vehicles/solid_blocks";
		if (!entity.isVehicle() || entity.getPersistentData().getDouble(nbtFuel) <= 0) {
			if (entity.getPersistentData().getDouble(nbtVehicleGear) != 0) {
				entity.getPersistentData().putDouble(nbtVehicleGear, 0);
			}
			if (!((world.getBlockState(BlockPos.containing(entity.getX(), entity.getY() - block_check_distance, entity.getZ()))).getBlock() instanceof LiquidBlock) && (world
					.getBlockState(BlockPos.containing(entity.getX(), entity.getY() - block_check_distance, entity.getZ())).isFaceSturdy(world, BlockPos.containing(entity.getX(), entity.getY() - block_check_distance, entity.getZ()), Direction.UP)
					|| (world.getBlockState(BlockPos.containing(entity.getX(), entity.getY() - block_check_distance, entity.getZ()))).is(BlockTags.create(new ResourceLocation((tagVehicleSolidBlocks).toLowerCase(java.util.Locale.ENGLISH)))))) {
				entity.makeStuckInBlock(Blocks.AIR.defaultBlockState(), new Vec3(0.25, 0.05, 0.25));
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 255, false, false));
				entity.setDeltaMovement(new Vec3(0, (entity.getDeltaMovement().y()), 0));
				{
					Entity _ent = entity;
					_ent.teleportTo((entity.getX()), (entity.getY()), (entity.getZ()));
					if (_ent instanceof ServerPlayer _serverPlayer)
						_serverPlayer.connection.teleport((entity.getX()), (entity.getY()), (entity.getZ()), _ent.getYRot(), _ent.getXRot());
				}
			} else {
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 255, false, false));
				entity.setDeltaMovement(new Vec3(0, (entity.getDeltaMovement().y()), 0));
			}
		}
		if (entity.isVehicle()
				&& entity.getPersistentData()
						.getDouble(nbtVehicleGear) != 0
				&& ((world.getBlockState(BlockPos.containing(entity.getX(), entity.getY() - block_check_distance, entity.getZ()))).getBlock() instanceof LiquidBlock
						|| (world.getBlockState(BlockPos.containing(entity.getX(), entity.getY() - block_check_distance, entity.getZ()))).is(BlockTags.create(new ResourceLocation("minecraft:air_blocks")))
						|| !world.getBlockState(BlockPos.containing(entity.getX(), entity.getY() - block_check_distance, entity.getZ())).isFaceSturdy(world, BlockPos.containing(entity.getX(), entity.getY() - block_check_distance, entity.getZ()),
								Direction.UP) && !(world.getBlockState(BlockPos.containing(entity.getX(), entity.getY() - block_check_distance, entity.getZ()))).is(BlockTags.create(new ResourceLocation("local:tagvehiclesolidblocks"))))) {
			entity.setDeltaMovement(new Vec3((entity.getDeltaMovement().x()), (0 - drop_force), (entity.getDeltaMovement().z())));
		}
	}
}
