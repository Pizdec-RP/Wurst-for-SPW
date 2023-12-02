/*

 *



 */
package net.purefps.modules;

import java.awt.Color;
import java.util.ArrayList;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.events.CameraTransformViewBobbingListener;
import net.purefps.events.RenderListener;
import net.purefps.events.UpdateListener;
import net.purefps.module.Hack;
import net.purefps.settings.ColorSetting;
import net.purefps.settings.EspBoxSizeSetting;
import net.purefps.settings.EspStyleSetting;
import net.purefps.util.EntityUtils;
import net.purefps.util.RegionPos;
import net.purefps.util.RenderUtils;
import net.purefps.util.RotationUtils;

@SearchTags({"item esp", "ItemTracers", "item tracers"})
public final class ItemEspHack extends Hack implements UpdateListener,
	CameraTransformViewBobbingListener, RenderListener
{
	private final EspStyleSetting style = new EspStyleSetting();
	
	private final EspBoxSizeSetting boxSize = new EspBoxSizeSetting(
		"\u00a7lAccurate\u00a7r mode shows the exact hitbox of each item.\n"
			+ "\u00a7lFancy\u00a7r mode shows larger boxes that look better.");
	
	private final ColorSetting color = new ColorSetting("Color",
		"Items will be highlighted in this color.", Color.YELLOW);
	
	private final ArrayList<ItemEntity> items = new ArrayList<>();
	
	public ItemEspHack()
	{
		super("ItemESP");
		setCategory(Category.RENDER);
		
		addSetting(style);
		addSetting(boxSize);
		addSetting(color);
	}
	
	@Override
	public void onEnable()
	{
		EVENTS.add(UpdateListener.class, this);
		EVENTS.add(CameraTransformViewBobbingListener.class, this);
		EVENTS.add(RenderListener.class, this);
	}
	
	@Override
	public void onDisable()
	{
		EVENTS.remove(UpdateListener.class, this);
		EVENTS.remove(CameraTransformViewBobbingListener.class, this);
		EVENTS.remove(RenderListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		items.clear();
		for(Entity entity : MC.world.getEntities())
			if(entity instanceof ItemEntity)
				items.add((ItemEntity)entity);
	}
	
	@Override
	public void onCameraTransformViewBobbing(
		CameraTransformViewBobbingEvent event)
	{
		if(style.hasLines())
			event.cancel();
	}
	
	@Override
	public void onRender(MatrixStack matrixStack, float partialTicks)
	{
		// GL settings
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		matrixStack.push();
		
		RegionPos region = RenderUtils.getCameraRegion();
		RenderUtils.applyRegionalRenderOffset(matrixStack, region);
		
		renderBoxes(matrixStack, partialTicks, region);
		
		if(style.hasLines())
			renderTracers(matrixStack, partialTicks, region);
		
		matrixStack.pop();
		
		// GL resets
		RenderSystem.setShaderColor(1, 1, 1, 1);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	private void renderBoxes(MatrixStack matrixStack, float partialTicks,
		RegionPos region)
	{
		float extraSize = boxSize.getExtraSize();
		
		for(ItemEntity e : items)
		{
			matrixStack.push();
			
			Vec3d lerpedPos = EntityUtils.getLerpedPos(e, partialTicks)
				.subtract(region.toVec3d());
			matrixStack.translate(lerpedPos.x, lerpedPos.y, lerpedPos.z);
			
			if(style.hasBoxes())
			{
				matrixStack.push();
				matrixStack.scale(e.getWidth() + extraSize,
					e.getHeight() + extraSize, e.getWidth() + extraSize);
				
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				float[] colorF = color.getColorF();
				RenderSystem.setShaderColor(colorF[0], colorF[1], colorF[2],
					0.5F);
				RenderUtils.drawOutlinedBox(new Box(-0.5, 0, -0.5, 0.5, 1, 0.5),
					matrixStack);
				
				matrixStack.pop();
			}
			
			matrixStack.pop();
		}
	}
	
	private void renderTracers(MatrixStack matrixStack, float partialTicks,
		RegionPos region)
	{
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		float[] colorF = color.getColorF();
		RenderSystem.setShaderColor(colorF[0], colorF[1], colorF[2], 0.5F);
		
		Matrix4f matrix = matrixStack.peek().getPositionMatrix();
		Tessellator tessellator = RenderSystem.renderThreadTesselator();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		RenderSystem.setShader(GameRenderer::getPositionProgram);
		
		Vec3d regionVec = region.toVec3d();
		Vec3d start = RotationUtils.getClientLookVec(partialTicks)
			.add(RenderUtils.getCameraPos()).subtract(regionVec);
		
		bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES,
			VertexFormats.POSITION);
		for(ItemEntity e : items)
		{
			Vec3d end = EntityUtils.getLerpedBox(e, partialTicks).getCenter()
				.subtract(regionVec);
			
			bufferBuilder
				.vertex(matrix, (float)start.x, (float)start.y, (float)start.z)
				.next();
			bufferBuilder
				.vertex(matrix, (float)end.x, (float)end.y, (float)end.z)
				.next();
		}
		tessellator.draw();
	}
}
