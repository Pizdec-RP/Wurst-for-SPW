/*

 *



 */
package net.purefps.settings.filters;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.ShulkerEntity;

public final class FilterShulkersSetting extends EntityFilterCheckbox
{
	public FilterShulkersSetting(String description, boolean checked)
	{
		super("Filter shulkers", description, checked);
	}
	
	@Override
	public boolean test(Entity e)
	{
		return !(e instanceof ShulkerEntity);
	}
	
	public static FilterShulkersSetting genericCombat(boolean checked)
	{
		return new FilterShulkersSetting("Won't attack shulkers.", checked);
	}
	
	public static FilterShulkersSetting genericVision(boolean checked)
	{
		return new FilterShulkersSetting("Won't show shulkers.", checked);
	}
}
