
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package com.sdkfz.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;

import com.sdkfz.SdkfzMod;

public class SdkfzModSounds {
	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SdkfzMod.MODID);
	public static final RegistryObject<SoundEvent> ENGINE = REGISTRY.register("engine", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("sdkfz", "engine")));
}
