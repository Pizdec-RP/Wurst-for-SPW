/*

 *



 */
package net.purefps.modules;

import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.events.UpdateListener;
import net.purefps.module.Hack;

@SearchTags({"fast place"})
public final class FastPlaceHack extends Hack implements UpdateListener
{
	public FastPlaceHack()
	{
		super("FastPlace");
		setCategory(Category.BLOCKS);
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
		MC.itemUseCooldown = 0;
	}
}
