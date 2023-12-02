// 
// Decompiled by Procyon v0.6.0
// 

package net.purefps.modules;

import java.util.Iterator;
import net.purefps.util.BlockVertexCompiler;
import net.purefps.util.BlockUtils;
import org.joml.Matrix4f;
import java.util.function.Supplier;
import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL11;
import net.purefps.util.RenderUtils;
import net.purefps.settings.Setting;
import net.purefps.Category;
import java.awt.Color;
import net.purefps.util.RegionPos;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.BufferBuilder.BuiltBuffer;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import net.minecraft.util.math.BlockPos;
import java.util.HashSet;
import net.purefps.settings.TextFieldSetting;
import net.purefps.settings.ColorSetting;
import net.purefps.SearchTags;
import net.purefps.events.RenderListener;
import net.purefps.events.UpdateListener;
import net.purefps.module.Hack;

@SearchTags({ "block esp", "factions" })
public final class BlockESP extends Hack implements UpdateListener, RenderListener
{
    private final ColorSetting color;
    private final TextFieldSetting bname;
    private final HashSet<BlockPos> matchingBlocks;
    private ArrayList<int[]> vertices;
    private VertexBuffer vertexBuffer;
    private RegionPos lastRegion;
    
    public BlockESP() {
        super("BlockESP");
        this.color = new ColorSetting("Color", "Man-made blocks will be highlighted in this color.", Color.RED);
        this.bname = new TextFieldSetting("blockname", "name of block", "sex");
        this.matchingBlocks = new HashSet<BlockPos>();
        this.vertices = new ArrayList<int[]>();
        this.setCategory(Category.RENDER);
        this.addSetting(this.color);
        this.addSetting(this.bname);
    }
    
    @Override
    public String getRenderName() {
        return "BlockESP";
    }
    
    public void onEnable() {
        BlockESP.EVENTS.add(UpdateListener.class, this);
        BlockESP.EVENTS.add(RenderListener.class, this);
    }
    
    public void onDisable() {
        BlockESP.EVENTS.remove(UpdateListener.class, this);
        BlockESP.EVENTS.remove(RenderListener.class, this);
        this.matchingBlocks.clear();
        this.vertices.clear();
        this.lastRegion = null;
        if (this.vertexBuffer != null) {
            this.vertexBuffer.close();
        }
    }
    
    @Override
	public void onRender(MatrixStack matrixStack, float partialTicks)
	{
		RegionPos region = RenderUtils.getCameraRegion();
		if(!region.equals(lastRegion))
			onUpdate();
		
		// GL settings
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		matrixStack.push();
		RenderUtils.applyRegionalRenderOffset(matrixStack, region);
		
		float[] colorF = color.getColorF();
		RenderSystem.setShader(GameRenderer::getPositionProgram);
		RenderSystem.setShaderColor(colorF[0], colorF[1], colorF[2], 0.15F);
		
		if(vertexBuffer != null)
		{
			Matrix4f viewMatrix = matrixStack.peek().getPositionMatrix();
			Matrix4f projMatrix = RenderSystem.getProjectionMatrix();
			ShaderProgram shader = RenderSystem.getShader();
			vertexBuffer.bind();
			vertexBuffer.draw(viewMatrix, projMatrix, shader);
			VertexBuffer.unbind();
		}
		
		matrixStack.pop();
		
		// GL resets
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		RenderSystem.setShaderColor(1, 1, 1, 1);
	}
    
    @Override
    public void onUpdate() {
    	int modulo = MC.player.age % 64;
		RegionPos region = RenderUtils.getCameraRegion();
		
		if(modulo == 0 || !region.equals(lastRegion))
		{
			if(vertexBuffer != null)
				vertexBuffer.close();
			
			vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
			
			Tessellator tessellator = RenderSystem.renderThreadTesselator();
			BufferBuilder bufferBuilder = tessellator.getBuffer();
			bufferBuilder.begin(VertexFormat.DrawMode.QUADS,
				VertexFormats.POSITION);
			
			for(int[] vertex : vertices)
				bufferBuilder.vertex(vertex[0] - region.x(), vertex[1],
					vertex[2] - region.z()).next();
			
			BuiltBuffer buffer = bufferBuilder.end();
			
			vertexBuffer.bind();
			vertexBuffer.upload(buffer);
			VertexBuffer.unbind();
			
			lastRegion = region;
		}
		
		// reset matching blocks
		if(modulo == 0)
			matchingBlocks.clear();
		
		int stepSize = MC.world.getHeight() / 64;
		int startY = MC.world.getTopY() - 1 - modulo * stepSize;
		int endY = startY - stepSize;
		
		BlockPos playerPos =
			BlockPos.ofFloored(MC.player.getX(), 0, MC.player.getZ());
		
        for (int y = startY; y > endY; --y) {
            for (int x = 64; x > -64; --x) {
                for (int z = 64; z > -64; --z) {
                	BlockPos pos = new BlockPos(playerPos.getX() + x, y,
    						playerPos.getZ() + z);
                    if (BlockUtils.getName(pos).contains(this.bname.getValue())) {
                        for (final BlockPos bp : new BlockPos[] { pos.up(), pos.down(), pos.west(), pos.east(), pos.north(), pos.south() }) {
                            final String name = BlockUtils.getName(bp);
                            if (name.contains("air") || name.contains("water")) {
                                this.matchingBlocks.add(pos);
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (modulo != 63) {
            return;
        }
        this.vertices = BlockVertexCompiler.compile(this.matchingBlocks);
    }
}
