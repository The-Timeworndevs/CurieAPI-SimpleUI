package net.timeworndevs.curiedisplay;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.timeworndevs.curiedisplay.client.RadiationHudOverlay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class CurieDisplayClient implements ClientModInitializer {
    public static final String MOD_ID = "curie-display";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    @Override
    public void onInitializeClient() {
        ClientLifecycleEvents.CLIENT_STARTED.register((client) -> {
            RadiationHudOverlay.init();
            for (RadiationHudOverlay.Overlay overlay: RadiationHudOverlay.overlays) {
                HudRenderCallback.EVENT.register(overlay);
            }
        });

    }
}