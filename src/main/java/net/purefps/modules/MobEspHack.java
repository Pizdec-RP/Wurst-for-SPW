/*

 *



 */
package net.purefps.modules;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.events.CameraTransformViewBobbingListener;
import net.purefps.events.RenderListener;
import net.purefps.events.UpdateListener;
import net.purefps.module.Hack;
import net.purefps.settings.EspBoxSizeSetting;
import net.purefps.settings.EspStyleSetting;
import net.purefps.settings.filterlists.EntityFilterList;
import net.purefps.settings.filters.AttackDetectingEntityFilter;
import net.purefps.settings.filters.FilterAllaysSetting;
import net.purefps.settings.filters.FilterArmorStandsSetting;
import net.purefps.settings.filters.FilterBatsSetting;
import net.purefps.settings.filters.FilterEndermenSetting;
import net.purefps.settings.filters.FilterGolemsSetting;
import net.purefps.settings.filters.FilterHostileSetting;
import net.purefps.settings.filters.FilterInvisibleSetting;
import net.purefps.settings.filters.FilterNamedSetting;
import net.purefps.settings.filters.FilterNeutralSetting;
import net.purefps.settings.filters.FilterPassiveSetting;
import net.purefps.settings.filters.FilterPassiveWaterSetting;
import net.purefps.settings.filters.FilterPetsSetting;
import net.purefps.settings.filters.FilterPiglinsSetting;
import net.purefps.settings.filters.FilterShulkersSetting;
import net.purefps.settings.filters.FilterSlimesSetting;
import net.purefps.settings.filters.FilterVillagersSetting;
import net.purefps.settings.filters.FilterZombiePiglinsSetting;
import net.purefps.settings.filters.FilterZombieVillagersSetting;
import net.purefps.util.EntityUtils;
import net.purefps.util.RegionPos;
import net.purefps.util.RenderUtils;
import net.purefps.util.RotationUtils;

@SearchTags({"mob esp", "MobTracers", "mob tracers"})
public final class MobEspHack extends Hack implements UpdateListener,
	CameraTransformViewBobbingListener, RenderListener
{
	private final EspStyleSetting style = new EspStyleSetting();

	private final EspBoxSizeSetting boxSize = new EspBoxSizeSetting(
		"\u00a7lAccurate\u00a7r mode shows the exact hitbox of each mob.\n"
			+ "\u00a7lFancy\u00a7r mode shows slightly larger boxes that look better.");

	private final EntityFilterList entityFilters =
		new EntityFilterList(FilterHostileSetting.genericVision(false),
			FilterNeutralSetting
				.genericVision(AttackDetectingEntityFilter.Mode.OFF),
			FilterPassiveSetting.genericVision(false),
			FilterPassiveWaterSetting.genericVision(false),
			FilterBatsSetting.genericVision(false),
			FilterSlimesSetting.genericVision(false),
			FilterPetsSetting.genericVision(false),
			FilterVillagersSetting.genericVision(false),
			FilterZombieVillagersSetting.genericVision(false),
			FilterGolemsSetting.genericVision(false),
			FilterPiglinsSetting
				.genericVision(AttackDetectingEntityFilter.Mode.OFF),
			FilterZombiePiglinsSetting
				.genericVision(AttackDetectingEntityFilter.Mode.OFF),
			FilterEndermenSetting
				.genericVision(AttackDetectingEntityFilter.Mode.OFF),
			FilterShulkersSetting.genericVision(false),
			FilterAllaysSetting.genericVision(false),
			FilterInvisibleSetting.genericVision(false),
			FilterNamedSetting.genericVision(false),
			FilterArmorStandsSetting.genericVision(true));

	private final ArrayList<LivingEntity> mobs = new ArrayList<>();
	private VertexBuffer mobBox;

	public MobEspHack()
	{
		super("MobESP");
		setCategory(Category.RENDER);
		addSetting(style);
		addSetting(boxSize);
		entityFilters.forEach(this::addSetting);
	}

	@Override
	public void onEnable()
	{
		EVENTS.add(UpdateListener.class, this);
		EVENTS.add(CameraTransformViewBobbingListener.class, this);
		EVENTS.add(RenderListener.class, this);

		mobBox = new VertexBuffer(VertexBuffer.Usage.STATIC);
		Box bb = new Box(-0.5, 0, -0.5, 0.5, 1, 0.5);
		RenderUtils.drawOutlinedBox(bb, mobBox);
	}

	@Override
	public void onDisable()
	{
		EVENTS.remove(UpdateListener.class, this);
		EVENTS.remove(CameraTransformViewBobbingListener.class, this);
		EVENTS.remove(RenderListener.class, this);

		if(mobBox != null)
			mobBox.close();
	}

	@Override
	public void onUpdate()
	{
		mobs.clear();

		Stream<LivingEntity> stream = StreamSupport
			.stream(MC.world.getEntities().spliterator(), false)
			.filter(LivingEntity.class::isInstance).map(e -> (LivingEntity)e)
			.filter(e -> !(e instanceof PlayerEntity))
			.filter(e -> !e.isRemoved() && e.getHealth() > 0);

		stream = entityFilters.applyTo(stream);

		mobs.addAll(stream.collect(Collectors.toList()));
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
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		matrixStack.push();

		RegionPos region = RenderUtils.getCameraRegion();
		RenderUtils.applyRegionalRenderOffset(matrixStack, region);

		if(style.hasBoxes())
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
		RenderSystem.setShader(GameRenderer::getPositionProgram);

		for(LivingEntity e : mobs)
		{
			matrixStack.push();

			Vec3d lerpedPos = EntityUtils.getLerpedPos(e, partialTicks)
				.subtract(region.toVec3d());
			matrixStack.translate(lerpedPos.x, lerpedPos.y, lerpedPos.z);

			matrixStack.scale(e.getWidth() + extraSize,
				e.getHeight() + extraSize, e.getWidth() + extraSize);

			float f = MC.player.distanceTo(e) / 20F;
			RenderSystem.setShaderColor(2 - f, f, 0, 0.5F);

			Matrix4f viewMatrix = matrixStack.peek().getPositionMatrix();
			Matrix4f projMatrix = RenderSystem.getProjectionMatrix();
			ShaderProgram shader = RenderSystem.getShader();
			mobBox.bind();
			mobBox.draw(viewMatrix, projMatrix, shader);
			VertexBuffer.unbind();

			matrixStack.pop();
		}
	}

	private void renderTracers(MatrixStack matrixStack, float partialTicks,
		RegionPos region)
	{
		RenderSystem.setShader(GameRenderer::getPositionColorProgram);
		RenderSystem.setShaderColor(1, 1, 1, 1);

		Matrix4f matrix = matrixStack.peek().getPositionMatrix();

		Tessellator tessellator = RenderSystem.renderThreadTesselator();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES,
			VertexFormats.POSITION_COLOR);

		Vec3d regionVec = region.toVec3d();
		Vec3d start = RotationUtils.getClientLookVec(partialTicks)
			.add(RenderUtils.getCameraPos()).subtract(regionVec);

		for(LivingEntity e : mobs)
		{
			Vec3d end = EntityUtils.getLerpedBox(e, partialTicks).getCenter()
				.subtract(regionVec);

			float f = MC.player.distanceTo(e) / 20F;
			float r = MathHelper.clamp(2 - f, 0, 1);
			float g = MathHelper.clamp(f, 0, 1);

			bufferBuilder
				.vertex(matrix, (float)start.x, (float)start.y, (float)start.z)
				.color(r, g, 0, 0.5F).next();

			bufferBuilder
				.vertex(matrix, (float)end.x, (float)end.y, (float)end.z)
				.color(r, g, 0, 0.5F).next();
		}

		tessellator.draw();
	}
}
