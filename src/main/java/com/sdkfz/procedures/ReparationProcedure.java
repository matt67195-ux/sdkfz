package com.sdkfz.procedures;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;

import javax.annotation.Nullable;

import com.sdkfz.entity.Sdkfz2Entity;

@Mod.EventBusSubscriber
public class ReparationProcedure {
	@SubscribeEvent
	public static void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {
		if (event.getHand() != event.getEntity().getUsedItemHand())
			return;
		execute(event, event.getLevel(), event.getTarget(), event.getEntity());
	}

	public static void execute(LevelAccessor world, Entity entity, Entity sourceentity) {
		execute(null, world, entity, sourceentity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity, Entity sourceentity) {
		if (entity == null || sourceentity == null)
			return;
		double health4 = 0;
		double health = 0;
		double health3 = 0;
		double health2 = 0;
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) < (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) && sourceentity.isShiftKeyDown()
				&& (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == Items.IRON_INGOT && entity instanceof Sdkfz2Entity) {
			if (event != null && event.isCancelable()) {
				event.setCanceled(true);
			} else if (event != null && event.hasResult()) {
				event.setResult(Event.Result.DENY);
			}
			health = (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) / 5;
			health2 = health * 2;
			health3 = health * 3;
			health4 = health * 4;
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) > 0 && (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) <= health) {
				if (entity instanceof LivingEntity _entity)
					_entity.setHealth((float) health2);
			} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) > health && (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) <= health2) {
				if (entity instanceof LivingEntity _entity)
					_entity.setHealth((float) health3);
			} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) > health2 && (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) <= health3) {
				if (entity instanceof LivingEntity _entity)
					_entity.setHealth((float) health4);
			} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) > health3 && (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) <= health4) {
				if (entity instanceof LivingEntity _entity)
					_entity.setHealth(entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1);
			}
			if (sourceentity instanceof LivingEntity _entity) {
				ItemStack _setstack = (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).copy();
				_setstack.setCount((int) ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getCount() - 1));
				_entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack);
				if (_entity instanceof Player _player)
					_player.getInventory().setChanged();
			}
			if (!world.isClientSide()) {
				if (world instanceof Level _level) {
					if (!_level.isClientSide()) {
						_level.playSound(null, BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.villager.work_toolsmith")), SoundSource.NEUTRAL, 1, 1);
					} else {
						_level.playLocalSound((entity.getX()), (entity.getY()), (entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.villager.work_toolsmith")), SoundSource.NEUTRAL, 1, 1, false);
					}
				}
			}
		}
	}
}
