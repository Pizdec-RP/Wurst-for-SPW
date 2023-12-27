/*

 *



 */
package net.purefps.settings.filters;

import net.minecraft.entity.Entity;

public final class FilterInvisibleSetting extends EntityFilterCheckbox
{
	public FilterInvisibleSetting(String description, boolean checked)
	{
		super("Filter invisible", description, checked);
	}

	@Override
	public boolean test(Entity e)
	{
		return !e.isInvisible();
	}

	public static FilterInvisibleSetting genericCombat(boolean checked)
	{
		return new FilterInvisibleSetting("Won't attack invisible entities.",
			checked);
	}

	public static FilterInvisibleSetting genericVision(boolean checked)
	{
		return new FilterInvisibleSetting("Won't show invisible entities.",
			checked);
	}
}
