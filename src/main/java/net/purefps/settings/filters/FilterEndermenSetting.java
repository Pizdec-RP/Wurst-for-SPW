/*

 *



 */
package net.purefps.settings.filters;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.EndermanEntity;

public final class FilterEndermenSetting extends AttackDetectingEntityFilter
{
	private FilterEndermenSetting(String description, Mode selected,
		boolean checked)
	{
		super("Filter endermen", description, selected, checked);
	}
	
	public FilterEndermenSetting(String description, Mode selected)
	{
		this(description, selected, false);
	}
	
	@Override
	public boolean onTest(Entity e)
	{
		return !(e instanceof EndermanEntity);
	}
	
	@Override
	public boolean ifCalmTest(Entity e)
	{
		return !(e instanceof EndermanEntity ee) || ee.isAttacking();
	}
	
	public static FilterEndermenSetting genericCombat(Mode selected)
	{
		return new FilterEndermenSetting("When set to \u00a7lOn\u00a7r,"
			+ " endermen won't be attacked at all.\n\n"
			+ "When set to \u00a7lIf calm\u00a7r, endermen won't be attacked"
			+ " until they attack first. Be warned that this filter cannot"
			+ " detect if the endermen are attacking you or someone else.\n\n"
			+ "When set to \u00a7lOff\u00a7r, this filter does nothing and"
			+ " endermen can be attacked.", selected);
	}
	
	public static FilterEndermenSetting genericVision(Mode selected)
	{
		return new FilterEndermenSetting("When set to \u00a7lOn\u00a7r,"
			+ " endermen won't be shown at all.\n\n"
			+ "When set to \u00a7lIf calm\u00a7r, endermen won't be shown"
			+ " until they attack something.\n\n"
			+ "When set to \u00a7lOff\u00a7r, this filter does nothing and"
			+ " endermen can be shown.", selected);
	}
	
	public static FilterEndermenSetting onOffOnly(String description,
		boolean onByDefault)
	{
		return new FilterEndermenSetting(description, null, onByDefault);
	}
}
