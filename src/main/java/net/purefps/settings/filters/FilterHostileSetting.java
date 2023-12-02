/*

 *



 */
package net.purefps.settings.filters;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.PiglinEntity;

public final class FilterHostileSetting extends EntityFilterCheckbox
{
	private static final String EXCEPTIONS_TEXT = "\n\nThis filter does not"
		+ " affect endermen, non-brute piglins, and zombified piglins.";
	
	public FilterHostileSetting(String description, boolean checked)
	{
		super("Filter hostile mobs", description + EXCEPTIONS_TEXT, checked);
	}
	
	@Override
	public boolean test(Entity e)
	{
		// never filter out neutral mobs (including piglins)
		if(e instanceof Angerable || e instanceof PiglinEntity)
			return false;
		
		return !(e instanceof Monster);
	}
	
	public static FilterHostileSetting genericCombat(boolean checked)
	{
		return new FilterHostileSetting(
			"Won't attack hostile mobs like zombies and creepers.", checked);
	}
	
	public static FilterHostileSetting genericVision(boolean checked)
	{
		return new FilterHostileSetting(
			"Won't show hostile mobs like zombies and creepers.", checked);
	}
}
