package net.timeworndevs.quantumui.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;
import net.timeworndevs.quantumui.Quantum;
import net.timeworndevs.quantumui.util.IEntityDataSaver;

public class RadiationAlphaHudOverlay implements HudRenderCallback {

    private static final Identifier FILLED_ALPHA = new Identifier(Quantum.MOD_ID,
            "textures/radiation/filled_alpha.png");

    private static final Identifier EMPTY_RADIATION = new Identifier(Quantum.MOD_ID,
            "textures/radiation/empty.png");


    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
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
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, EMPTY_RADIATION);
        for (int i=0; i< 10; i++) {
            drawContext.drawTexture(EMPTY_RADIATION,x/20 + (i * 10), y/20, 0, 0, 10, 10, 10, 10);
        }

        RenderSystem.setShaderTexture(0, FILLED_ALPHA);
        for(int i = 0; i < 10; i++) {
            if(((IEntityDataSaver) MinecraftClient.getInstance().player).getPersistentData().getInt("radiation.alpha") > i*1000) {
                int maxProgress = 1000;
                int progress = ((IEntityDataSaver) MinecraftClient.getInstance().player).getPersistentData().getInt("radiation.alpha")-i*maxProgress;

                int nwidth = progress != 0 ? progress * 10 / maxProgress : 0;
                drawContext.drawTexture(FILLED_ALPHA,x/20 + (i * 10),y/20,0,0, nwidth,10, 10, 10);
            } else {
                break;
            }
        }
    }
}
