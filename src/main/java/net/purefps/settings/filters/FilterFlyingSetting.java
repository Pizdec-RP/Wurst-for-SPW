/*

 *



 */
package net.purefps.settings.filters;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.purefps.PFPSClient;
import net.purefps.settings.Setting;
import net.purefps.settings.SliderSetting;
import net.purefps.settings.filterlists.EntityFilterList.EntityFilter;

public final class FilterFlyingSetting extends SliderSetting
	implements EntityFilter
{
	public FilterFlyingSetting(String description, double value)
	{
		super("Filter flying", description, value, 0, 2, 0.05,
			ValueDisplay.DECIMAL.withLabel(0, "off"));
	}

	@Override
	public boolean test(Entity e)
	{
		if(!(e instanceof PlayerEntity))
			return true;

		Box box = e.getBoundingBox();
		box = box.union(box.offset(0, -getValue(), 0));
		return !PFPSClient.MC.world.isSpaceEmpty(box);
	}

	@Override
	public boolean isFilterEnabled()
	{
		return getValue() > 0;
	}

	@Override
	public Setting getSetting()
	{
		return this;
	}

	public static FilterFlyingSetting genericCombat(double value)
	{
		return new FilterFlyingSetting(
			"Won't attack players that are at least the given distance above ground.\n\n"
				+ "Useful for servers that try to detect your hacks by placing a flying bot near you.",
			value);
	}
}
