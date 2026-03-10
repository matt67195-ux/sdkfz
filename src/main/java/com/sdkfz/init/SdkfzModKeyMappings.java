
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package com.sdkfz.init;

import org.lwjgl.glfw.GLFW;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;

import com.sdkfz.network.ShiftkeyMessage;
import com.sdkfz.SdkfzMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class SdkfzModKeyMappings {
	public static final KeyMapping SHIFTKEY = new KeyMapping("key.sdkfz.shiftkey", GLFW.GLFW_KEY_SPACE, "key.categories.vehicles") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				SdkfzMod.PACKET_HANDLER.sendToServer(new ShiftkeyMessage(0, 0));
				ShiftkeyMessage.pressAction(Minecraft.getInstance().player, 0, 0);
				SHIFTKEY_LASTPRESS = System.currentTimeMillis();
			} else if (isDownOld != isDown && !isDown) {
				int dt = (int) (System.currentTimeMillis() - SHIFTKEY_LASTPRESS);
				SdkfzMod.PACKET_HANDLER.sendToServer(new ShiftkeyMessage(1, dt));
				ShiftkeyMessage.pressAction(Minecraft.getInstance().player, 1, dt);
			}
			isDownOld = isDown;
		}
	};
	private static long SHIFTKEY_LASTPRESS = 0;

	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		event.register(SHIFTKEY);
	}

	@Mod.EventBusSubscriber({Dist.CLIENT})
	public static class KeyEventListener {
		@SubscribeEvent
		public static void onClientTick(TickEvent.ClientTickEvent event) {
			if (Minecraft.getInstance().screen == null) {
				SHIFTKEY.consumeClick();
			}
		}
	}
}
