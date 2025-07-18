package net.timeworndevs.curiedisplay.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import net.timeworndevs.curieapi.radiation.RadiationNBT;
import net.timeworndevs.curieapi.radiation.RadiationType;
import net.timeworndevs.curieapi.util.CurieAPIConfig;
import net.timeworndevs.curieapi.util.IEntityDataSaver;
import net.timeworndevs.curiedisplay.CurieDisplayClient;

import java.util.ArrayList;
import java.util.List;

import static net.timeworndevs.curieapi.util.CurieAPIConfig.RADIATION_TYPES;

@Environment(EnvType.CLIENT)
public class RadiationHudOverlay {
    public static class Overlay implements HudRenderCallback {
        private static final Identifier TEXTURE = new Identifier(CurieDisplayClient.MOD_ID, "textures/gui/radiation_bar.png");
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
            MinecraftClient client = MinecraftClient.getInstance();
            if (client != null) {
                int width = client.getWindow().getScaledWidth();
                int height = client.getWindow().getScaledHeight() / 20;

                int x = width/40; // x -> right corner; 0 -> left corner
                // 0 -> top; y -> bottom
                RenderSystem.setShader(GameRenderer::getPositionTexProgram);
                RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
                RenderSystem.setShaderTexture(0, TEXTURE);
                MatrixStack matrices = drawContext.getMatrices();
                matrices.push();
                    matrices.scale(2.0f, 2.0f, 1.0f);
                    drawContext.drawTexture(TEXTURE, x,height + 6 * this.id, 0, 0, 54,4, 64, 16);

                    List<Float> color = this.type.getColor();
                    float r = color.get(0);
                    float g = color.get(1);
                    float b = color.get(2);
                    float a = color.get(3);

                    RenderSystem.setShaderColor(r, g, b, a);

                    int radiation = RadiationNBT.get((IEntityDataSaver) client.player, this.type.getName());
                    if (radiation > 0) {
                        float percent = (float) radiation / CurieAPIConfig.CAP;
                        drawContext.drawTexture(TEXTURE, x + 3, height + 6 * this.id + 1, 0, 4, (int)(percent * 48), 2, 64, 16);
                    }
                matrices.pop();
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            }
        }
    }
    public static ArrayList<Overlay> overlays = new ArrayList<>();
    public static void init() {
        int id = 0;

        for (RadiationType type : RADIATION_TYPES.values()) {
            overlays.add(new Overlay(id, type));
            id++;
        }
    }

}
