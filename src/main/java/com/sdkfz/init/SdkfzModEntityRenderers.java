
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package com.sdkfz.init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;

import com.sdkfz.client.renderer.Sdkfz2Renderer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SdkfzModEntityRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(SdkfzModEntities.SDKFZ_2.get(), Sdkfz2Renderer::new);
	}
}
