package com.sdkfz.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
// ShiftonpressedProcedure ya no necesita hacer nada en el servidor:
// la barra de progreso ahora se dibuja directamente en el cliente
// desde SdkfzModKeyMappings, y ShiftonreleasedProcedure maneja el cambio.
// Este archivo se mantiene por compatibilidad con el registro de mensajes.

public class ShiftonpressedProcedure {
    public static void execute(LevelAccessor world, Entity entity) {
        // La barra de progreso se maneja en el cliente (SdkfzModKeyMappings).
        // El cambio de marcha se ejecuta en ShiftonreleasedProcedure al soltar.
    }
}
