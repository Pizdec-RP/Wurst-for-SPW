/*

 *



 */
package net.purefps.modules;

import net.minecraft.util.math.Vec3d;
import net.purefps.Category;
import net.purefps.events.UpdateListener;
import net.purefps.module.Hack;

public final class NoWebHack extends Hack implements UpdateListener
{
	public NoWebHack()
	{
		super("NoWeb");
		setCategory(Category.MOVEMENT);
	}

	@Override
	public void onEnable()
	{
		EVENTS.add(UpdateListener.class, this);
	}

	@Override
	public void onDisable()
	{
		EVENTS.remove(UpdateListener.class, this);
	}

	@Override
	public void onUpdate()
	{
		MC.player.movementMultiplier = Vec3d.ZERO;
	}
}
