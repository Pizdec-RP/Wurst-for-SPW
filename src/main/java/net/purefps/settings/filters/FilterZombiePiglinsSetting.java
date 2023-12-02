/*

 *



 */
package net.purefps.settings.filters;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;

public final class FilterZombiePiglinsSetting
	extends AttackDetectingEntityFilter
{
	private FilterZombiePiglinsSetting(String description, Mode selected,
		boolean checked)
	{
		super("Filter zombie piglins", description, selected, checked);
	}
	
	public FilterZombiePiglinsSetting(String description, Mode selected)
	{
		this(description, selected, false);
	}
	
	@Override
	public boolean onTest(Entity e)
	{
		return !(e instanceof ZombifiedPiglinEntity);
	}
	
	@Override
	public boolean ifCalmTest(Entity e)
	{
		return !(e instanceof ZombifiedPiglinEntity zpe) || zpe.isAttacking();
	}
	
	public static FilterZombiePiglinsSetting genericCombat(Mode selected)
	{
		return new FilterZombiePiglinsSetting("When set to \u00a7lOn\u00a7r,"
			+ " zombified piglins won't be attacked at all.\n\n"
			+ "When set to \u00a7lIf calm\u00a7r, zombified piglins won't be"
			+ " attacked until they attack first. Be warned that this filter"
			+ " cannot detect if the zombified piglins are attacking you or"
			+ " someone else.\n\n"
			+ "When set to \u00a7lOff\u00a7r, this filter does nothing and"
			+ " zombified piglins can be attacked.", selected);
	}
	
	public static FilterZombiePiglinsSetting genericVision(Mode selected)
	{
		return new FilterZombiePiglinsSetting("When set to \u00a7lOn\u00a7r,"
			+ " zombified piglins won't be shown at all.\n\n"
			+ "When set to \u00a7lIf calm\u00a7r, zombified piglins won't be"
			+ " shown until they attack something.\n\n"
			+ "When set to \u00a7lOff\u00a7r, this filter does nothing and"
			+ " zombified piglins can be shown.", selected);
	}
	
	public static FilterZombiePiglinsSetting onOffOnly(String description,
		boolean onByDefault)
	{
		return new FilterZombiePiglinsSetting(description, null, onByDefault);
	}
}
