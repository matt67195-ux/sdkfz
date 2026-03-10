
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package com.sdkfz.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.Item;

import com.sdkfz.item.OilBucketItem;
import com.sdkfz.item.KettenkradspawnitemItem;
import com.sdkfz.SdkfzMod;

public class SdkfzModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, SdkfzMod.MODID);
	public static final RegistryObject<Item> KETTENKRADSPAWNITEM = REGISTRY.register("kettenkradspawnitem", () -> new KettenkradspawnitemItem());
	public static final RegistryObject<Item> OIL_BUCKET = REGISTRY.register("oil_bucket", () -> new OilBucketItem());
	// Start of user code block custom items
	// End of user code block custom items
}
