/*

 *



 */
package net.purefps.modules;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.purefps.Category;
import net.purefps.events.RenderListener;
import net.purefps.events.UpdateListener;
import net.purefps.module.Hack;
import net.purefps.util.OverlayRenderer;

public final class OverlayHack extends Hack
	implements UpdateListener, RenderListener
{
	private final OverlayRenderer renderer = new OverlayRenderer();

	public OverlayHack()
	{
		super("Overlay");
		setCategory(Category.RENDER);
	}

	@Override
	public void onEnable()
	{
		EVENTS.add(UpdateListener.class, this);
		EVENTS.add(RenderListener.class, this);
	}

	@Override
	public void onDisable()
	{
		EVENTS.remove(UpdateListener.class, this);
		EVENTS.remove(RenderListener.class, this);
		renderer.resetProgress();
	}

	@Override
	public void onUpdate()
	{
		if(MC.interactionManager.isBreakingBlock())
			renderer.updateProgress();
		else
			renderer.resetProgress();
	}

	@Override
	public void onRender(MatrixStack matrixStack, float partialTicks)
	{
		if(!MC.interactionManager.isBreakingBlock() || !(MC.crosshairTarget instanceof BlockHitResult blockHitResult)
			|| blockHitResult.getType() != HitResult.Type.BLOCK)
			return;

		renderer.render(matrixStack, partialTicks,
			blockHitResult.getBlockPos());
	}
}
