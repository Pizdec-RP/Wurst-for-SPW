/*

 *



 */
package net.purefps.settings.filters;

import net.minecraft.entity.Entity;

public final class FilterNamedSetting extends EntityFilterCheckbox
{
	public FilterNamedSetting(String description, boolean checked)
	{
		super("Filter named", description, checked);
	}

	@Override
	public boolean test(Entity e)
	{
		return !e.hasCustomName();
	}

	public static FilterNamedSetting genericCombat(boolean checked)
	{
		return new FilterNamedSetting("Won't attack name-tagged entities.",
			checked);
	}

	public static FilterNamedSetting genericVision(boolean checked)
	{
		return new FilterNamedSetting("Won't show name-tagged entities.",
			checked);
	}
}
