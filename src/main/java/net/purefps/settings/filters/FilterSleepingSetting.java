/*

 *



 */
package net.purefps.settings.filters;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;

public final class FilterSleepingSetting extends EntityFilterCheckbox
{
	public FilterSleepingSetting(String description, boolean checked)
	{
		super("Filter sleeping", description, checked);
	}

	@Override
	public boolean test(Entity e)
	{
		if(!(e instanceof PlayerEntity pe))
			return true;

		return !pe.isSleeping() && pe.getPose() != EntityPose.SLEEPING;
	}

	public static FilterSleepingSetting genericCombat(boolean checked)
	{
		return new FilterSleepingSetting("Won't attack sleeping players.\n\n"
			+ "Useful for servers like Mineplex that place sleeping players on"
			+ " the ground to make them look like corpses.", checked);
	}

	public static FilterSleepingSetting genericVision(boolean checked)
	{
		return new FilterSleepingSetting("Won't show sleeping players.",
			checked);
	}
}
