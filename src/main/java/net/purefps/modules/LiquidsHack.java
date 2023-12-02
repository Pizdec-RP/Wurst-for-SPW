/*

 *



 */
package net.purefps.modules;

import net.purefps.Category;
import net.purefps.events.HitResultRayTraceListener;
import net.purefps.module.Hack;

public final class LiquidsHack extends Hack implements HitResultRayTraceListener
{
	public LiquidsHack()
	{
		super("Liquids");
		setCategory(Category.BLOCKS);
	}
	
	@Override
	protected void onEnable()
	{
		EVENTS.add(HitResultRayTraceListener.class, this);
	}
	
	@Override
	protected void onDisable()
	{
		EVENTS.remove(HitResultRayTraceListener.class, this);
	}
	
	@Override
	public void onHitResultRayTrace(float partialTicks)
	{
		float reach = MC.interactionManager.getReachDistance();
		MC.crosshairTarget =
			MC.getCameraEntity().raycast(reach, partialTicks, true);
	}
}
