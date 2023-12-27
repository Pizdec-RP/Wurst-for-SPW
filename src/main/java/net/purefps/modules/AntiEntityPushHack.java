/*

 *



 */
package net.purefps.modules;

import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.events.VelocityFromEntityCollisionListener;
import net.purefps.module.Hack;

@SearchTags({"anti entity push", "NoEntityPush", "no entity push"})
public final class AntiEntityPushHack extends Hack
	implements VelocityFromEntityCollisionListener
{
	public AntiEntityPushHack()
	{
		super("AntiEntityPush");
		setCategory(Category.MOVEMENT);
	}

	@Override
	protected void onEnable()
	{
		EVENTS.add(VelocityFromEntityCollisionListener.class, this);
	}

	@Override
	protected void onDisable()
	{
		EVENTS.remove(VelocityFromEntityCollisionListener.class, this);
	}

	@Override
	public void onVelocityFromEntityCollision(
		VelocityFromEntityCollisionEvent event)
	{
		if(event.getEntity() == MC.player)
			event.cancel();
	}
}
