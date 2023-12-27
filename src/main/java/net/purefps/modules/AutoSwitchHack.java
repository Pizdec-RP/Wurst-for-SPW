/*

 *



 */
package net.purefps.modules;

import net.minecraft.entity.player.PlayerInventory;
import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.events.UpdateListener;
import net.purefps.module.Hack;

@SearchTags({"auto switch"})
public final class AutoSwitchHack extends Hack implements UpdateListener
{
	public AutoSwitchHack()
	{
		super("AutoSwitch");
		setCategory(Category.ITEMS);
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
		PlayerInventory inventory = MC.player.getInventory();

		if(inventory.selectedSlot == 8)
			inventory.selectedSlot = 0;
		else
			inventory.selectedSlot++;
	}
}
