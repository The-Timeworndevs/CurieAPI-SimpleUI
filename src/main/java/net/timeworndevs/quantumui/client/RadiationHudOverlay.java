package net.timeworndevs.quantumui.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;
import net.timeworndevs.quantum.radiation.RadiationNBT;
import net.timeworndevs.quantum.radiation.RadiationType;
import net.timeworndevs.quantum.util.IEntityDataSaver;
import net.timeworndevs.quantum.util.QuantumConfig;
import net.timeworndevs.quantumui.QuantumUI;

import java.util.ArrayList;
import java.util.List;

import static net.timeworndevs.quantum.radiation.RadiationType.RADIATION_TYPES;
public class RadiationHudOverlay {
    public static class Overlay implements HudRenderCallback {
        private static final int maxProgress = QuantumConfig.cap/10;
        private static final Identifier FILLED_DEFAULT = new Identifier(QuantumUI.MOD_ID,
                "textures/radiation/filled.png");

        private static final Identifier EMPTY_RADIATION = new Identifier(QuantumUI.MOD_ID,
                "textures/radiation/empty.png");
        private final int id;
        private final RadiationType type;
        public Overlay(int id, RadiationType type) {
            this.id = id;
            this.type = type;
        }
        public RadiationType getRadiationType() {
            return this.type;
        }


        @Override
        public void onHudRender(DrawContext drawContext, float tickDelta) {
//            Matrix4f positionMatrix = drawContext.getMatrices().peek().getPositionMatrix();
            int x = 0;
            int y = 0;
            MinecraftClient client = MinecraftClient.getInstance();
            if (client != null) {
                int width = client.getWindow().getScaledWidth();
                int height = client.getWindow().getScaledHeight();
                x = width/2; // x -> right corner; 0 -> left corner
                y = height; // 0 -> top; y -> bottom
            }
            RenderSystem.setShader(GameRenderer::getPositionTexProgram);
            RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
            RenderSystem.setShaderTexture(0, EMPTY_RADIATION);
            for (int i=0; i < 10; i++) {
                drawContext.drawTexture(EMPTY_RADIATION,x/20 + (i * 10), y/20+5*this.id, 0, 0, 10, 10, 10, 10);
            }
            List<Float> color = this.type.getColor();
            float r = color.get(0);
            float g = color.get(1);
            float b = color.get(2);
            float a = color.get(3);

            RenderSystem.setShaderColor(r, g, b, a);
            RenderSystem.setShaderTexture(0, FILLED_DEFAULT);

            for(int i = 0; i < 10; i++) {

                int radiation = RadiationNBT.get((IEntityDataSaver) MinecraftClient.getInstance().player, type.getName());
                if(radiation > i * maxProgress) {

                    int progress = radiation-i * maxProgress;

                    int nwidth = progress != 0 ? progress * 10 / maxProgress : 0;
                    drawContext.drawTexture(FILLED_DEFAULT,x/20 + (i * 10),y/20+5*this.id,0,0, nwidth,10, 10, 10);

                } else {
                    break;
                }
            }
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
    public static ArrayList<Overlay> overlays = new ArrayList<>();
    public static void init() {
        int id = 0;

        for (RadiationType type : RADIATION_TYPES.values()) {
            QuantumUI.LOGGER.info("Radiation: " + type.getName());
            overlays.add(new Overlay(id, type));
            id++;
        }
    }

}
