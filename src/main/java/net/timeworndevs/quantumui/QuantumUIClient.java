package net.timeworndevs.quantumui;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.timeworndevs.quantumui.client.RadiationHudOverlay;

@Environment(EnvType.CLIENT)
public class QuantumUIClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientLifecycleEvents.CLIENT_STARTED.register((client) -> {
            RadiationHudOverlay.init();
            for (RadiationHudOverlay.Overlay overlay: RadiationHudOverlay.overlays) {
                QuantumUI.LOGGER.debug(overlay.toString());
                HudRenderCallback.EVENT.register(overlay);
            }
        });

    }
}