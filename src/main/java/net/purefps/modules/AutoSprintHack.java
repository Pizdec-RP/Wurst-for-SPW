/*

 *



 */
package net.purefps.modules;

import net.minecraft.client.network.ClientPlayerEntity;
import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.events.UpdateListener;
import net.purefps.module.Hack;

@SearchTags({"auto sprint"})
public final class AutoSprintHack extends Hack implements UpdateListener
{
	public AutoSprintHack()
	{
		super("AutoSprint");
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
		ClientPlayerEntity player = MC.player;

		if(player.horizontalCollision || player.isSneaking() || player.isInsideWaterOrBubbleColumn() || player.isSubmergedInWater())
			return;

		if(player.forwardSpeed > 0)
			player.setSprinting(true);
	}
}
