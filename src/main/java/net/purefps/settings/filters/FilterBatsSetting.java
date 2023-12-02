/*

 *



 */
package net.purefps.settings.filters;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.AmbientEntity;

public final class FilterBatsSetting extends EntityFilterCheckbox
{
	public FilterBatsSetting(String description, boolean checked)
	{
		super("Filter bats", description, checked);
	}
	
	@Override
	public boolean test(Entity e)
	{
		return !(e instanceof AmbientEntity);
	}
	
	public static FilterBatsSetting genericCombat(boolean checked)
	{
		return new FilterBatsSetting("Won't attack bats and any other"
			+ " \"ambient\" mobs that might be added by mods.", checked);
	}
	
	public static FilterBatsSetting genericVision(boolean checked)
	{
		return new FilterBatsSetting("Won't show bats and any other"
			+ " \"ambient\" mobs that might be added by mods.", checked);
	}
}
