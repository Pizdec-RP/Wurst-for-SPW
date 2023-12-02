/*

 *



 */
package net.purefps.settings.filters;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.ZombieVillagerEntity;

public final class FilterZombieVillagersSetting extends EntityFilterCheckbox
{
	public FilterZombieVillagersSetting(String description, boolean checked)
	{
		super("Filter zombie villagers", description, checked);
	}
	
	@Override
	public boolean test(Entity e)
	{
		return !(e instanceof ZombieVillagerEntity);
	}
	
	public static FilterZombieVillagersSetting genericCombat(boolean checked)
	{
		return new FilterZombieVillagersSetting(
			"Won't attack zombified villagers.", checked);
	}
	
	public static FilterZombieVillagersSetting genericVision(boolean checked)
	{
		return new FilterZombieVillagersSetting(
			"Won't show zombified villagers.", checked);
	}
}
