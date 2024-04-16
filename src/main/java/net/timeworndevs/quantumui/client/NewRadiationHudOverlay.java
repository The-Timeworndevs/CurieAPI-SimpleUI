package net.timeworndevs.quantumui.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;
import net.timeworndevs.quantumui.Quantum;
import net.timeworndevs.quantumui.util.IEntityDataSaver;
import org.joml.Matrix4f;

import java.util.HashMap;

public class NewRadiationHudOverlay {

    public static class NewOverlay implements HudRenderCallback {
        private static final Identifier FILLED_DEFAULT = new Identifier(Quantum.MOD_ID,
                "textures/radiation/filled.png");

        private static final Identifier EMPTY_RADIATION = new Identifier(Quantum.MOD_ID,
                "textures/radiation/empty.png");
        private final int id;
        private final String kind;
        public NewOverlay(int id, String kind) {
            this.id = id;
            this.kind = kind;
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
            for (int i=0; i< 10; i++) {
                drawContext.drawTexture(EMPTY_RADIATION,x/20 + (i * 10), y/20+5*this.id, 0, 0, 10, 10, 10, 10);
            }
            if (Quantum.new_radiation_types.get(this.kind).has("color")) {
                float r = Float.parseFloat(Quantum.new_radiation_types.get(this.kind).get("color").getAsString().split("f")[0]);
                float g = Float.parseFloat(Quantum.new_radiation_types.get(this.kind).get("color").getAsString().split("f")[1]);
                float b = Float.parseFloat(Quantum.new_radiation_types.get(this.kind).get("color").getAsString().split("f")[2]);
                float a = Float.parseFloat(Quantum.new_radiation_types.get(this.kind).get("color").getAsString().split("f")[3]);
                RenderSystem.setShaderColor(r,g,b,a);
            }
            RenderSystem.setShaderTexture(0, FILLED_DEFAULT);
            for(int i = 0; i < 10; i++) {
                if(((IEntityDataSaver) MinecraftClient.getInstance().player).getPersistentData().getInt("radiation." + this.kind) > i*1000) {
                    int maxProgress = 1000;
                    int progress = ((IEntityDataSaver) MinecraftClient.getInstance().player).getPersistentData().getInt("radiation." + this.kind)-i*maxProgress;

                    int nwidth = progress != 0 ? progress * 10 / maxProgress : 0;
                    drawContext.drawTexture(FILLED_DEFAULT,x/20 + (i * 10),y/20+5*this.id,0,0, nwidth,10, 10, 10);

                } else {
                    break;
                }
            }
            RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        }
    }
    public static HashMap<String, NewOverlay> overlays = new HashMap<>();
    public static void init() {
        int id = 0;

        for (String kind: Quantum.new_radiation_types.keySet()) {
            Quantum.LOGGER.info(kind + "INIT");
            overlays.put(kind, new NewOverlay(id, kind));
            Quantum.LOGGER.info(kind + id);
            id++;
        }
    }

}
