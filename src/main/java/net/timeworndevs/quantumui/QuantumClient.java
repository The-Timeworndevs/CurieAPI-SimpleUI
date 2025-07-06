package net.timeworndevs.quantumui;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.timeworndevs.quantumui.client.NewRadiationHudOverlay;

@Environment(EnvType.CLIENT)
public class QuantumClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Here we will put client-only registration code (thabks toast)
        for (String i: NewRadiationHudOverlay.overlays.keySet()) {
            HudRenderCallback.EVENT.register(NewRadiationHudOverlay.overlays.get(i));
        }
        //HandledScreens.register(ModScreenHandlers.MICROWAVE_SCREEN_HANDLER, MicrowaveScreen::new);
    }
}