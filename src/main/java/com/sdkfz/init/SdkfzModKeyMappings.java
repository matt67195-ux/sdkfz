/*
 *	MCreator note: This file will be REGENERATED on each build.
 *  IMPORTANTE: Si MCreator regenera este archivo, volver a aplicar estos cambios.
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
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import com.sdkfz.network.ShiftkeyMessage;
import com.sdkfz.SdkfzMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class SdkfzModKeyMappings {

    // Frames de la barra (10 pasos = ~500ms a 20tps, cada tick es 50ms)
    private static final String[] BAR_FRAMES = {
            "Shifting: \u2588\u2593\u2592\u2591\u2591\u2591\u2591\u2591\u2591\u2591 \u25B2",
            "Shifting: \u2588\u2588\u2593\u2592\u2591\u2591\u2591\u2591\u2591\u2591 \u25B2",
            "Shifting: \u2588\u2588\u2588\u2593\u2592\u2591\u2591\u2591\u2591\u2591 \u25B2",
            "Shifting: \u2588\u2588\u2588\u2588\u2593\u2592\u2591\u2591\u2591\u2591 \u25B2",
            "Shifting: \u2588\u2588\u2588\u2588\u2588\u2593\u2592\u2591\u2591\u2591 \u25B2",
            "Shifting: \u2588\u2588\u2588\u2588\u2588\u2588\u2593\u2592\u2591\u2591 \u25B2",
            "Shifting: \u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2593\u2592\u2591 \u25B2",
            "Shifting: \u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2593\u2592 \u25B2",
            "Shifting: \u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2593 \u25B2",
            "Shifting: \u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588 \u25BC",
    };

    private static boolean keyWasDown = false;
    private static long pressStartTime = 0;
    private static boolean isPressingShift = false;

    public static final KeyMapping SHIFTKEY = new KeyMapping("key.sdkfz.shiftkey", GLFW.GLFW_KEY_SPACE, "key.categories.vehicles");
    private static long SHIFTKEY_LASTPRESS = 0;

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(SHIFTKEY);
    }

    @Mod.EventBusSubscriber({Dist.CLIENT})
    public static class KeyEventListener {
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase != TickEvent.Phase.END) return;
            if (Minecraft.getInstance().screen != null) return;

            Player player = Minecraft.getInstance().player;
            if (player == null || !player.isPassenger()) {
                keyWasDown = false;
                isPressingShift = false;
                return;
            }

            boolean isDown = SHIFTKEY.isDown();

            // Flanco de bajada: acaba de presionar
            if (isDown && !keyWasDown) {
                pressStartTime = System.currentTimeMillis();
                isPressingShift = true;
                SHIFTKEY_LASTPRESS = pressStartTime;
                // Notificar al servidor que empezó el press
                SdkfzMod.PACKET_HANDLER.sendToServer(new ShiftkeyMessage(0, 0));
            }

            // Mientras está presionado: actualizar barra en el cliente directamente
            if (isDown && isPressingShift) {
                long held = System.currentTimeMillis() - pressStartTime;
                // 500ms = barra llena. Cada frame es ~50ms
                int frame = (int) Math.min(held / 50, BAR_FRAMES.length - 1);
                player.displayClientMessage(Component.literal(BAR_FRAMES[frame]), true);
            }

            // Flanco de subida: soltó la tecla
            if (!isDown && keyWasDown && isPressingShift) {
                isPressingShift = false;
                int dt = (int) (System.currentTimeMillis() - SHIFTKEY_LASTPRESS);
                SdkfzMod.PACKET_HANDLER.sendToServer(new ShiftkeyMessage(1, dt));
            }

            keyWasDown = isDown;

            if (Minecraft.getInstance().screen == null) {
                SHIFTKEY.consumeClick();
            }
        }
    }
}
