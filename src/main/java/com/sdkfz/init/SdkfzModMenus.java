
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package com.sdkfz.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.common.extensions.IForgeMenuType;

import net.minecraft.world.inventory.MenuType;

import com.sdkfz.world.inventory.InventoryGuiMenu;
import com.sdkfz.SdkfzMod;

public class SdkfzModMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, SdkfzMod.MODID);
	public static final RegistryObject<MenuType<InventoryGuiMenu>> INVENTORY_GUI = REGISTRY.register("inventory_gui", () -> IForgeMenuType.create(InventoryGuiMenu::new));
}
