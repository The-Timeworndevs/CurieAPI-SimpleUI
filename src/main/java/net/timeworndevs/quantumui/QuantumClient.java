package net.timeworndevs.quantumui;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.timeworndevs.quantumui.client.NewRadiationHudOverlay;
import net.timeworndevs.quantumui.client.RadiationAlphaHudOverlay;
import net.timeworndevs.quantumui.client.RadiationBetaHudOverlay;
import net.timeworndevs.quantumui.client.RadiationGammaOverlay;

@Environment(EnvType.CLIENT)
public class QuantumClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Here we will put client-only registration code (thabks toast)
        //HudRenderCallback.EVENT.register(new RadiationAlphaHudOverlay());
        //HudRenderCallback.EVENT.register(new RadiationBetaHudOverlay());
        //HudRenderCallback.EVENT.register(new RadiationGammaOverlay());
        for (String i: NewRadiationHudOverlay.overlays.keySet()) {
            HudRenderCallback.EVENT.register(NewRadiationHudOverlay.overlays.get(i));
        }
        //HandledScreens.register(ModScreenHandlers.MICROWAVE_SCREEN_HANDLER, MicrowaveScreen::new);
    }
}