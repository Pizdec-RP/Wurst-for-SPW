/*

 *



 */
package net.purefps.settings.filters;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.MerchantEntity;

public final class FilterVillagersSetting extends EntityFilterCheckbox
{
	public FilterVillagersSetting(String description, boolean checked)
	{
		super("Filter villagers", description, checked);
	}
	
	@Override
	public boolean test(Entity e)
	{
		return !(e instanceof MerchantEntity);
	}
	
	public static FilterVillagersSetting genericCombat(boolean checked)
	{
		return new FilterVillagersSetting(
			"Won't attack villagers and wandering traders.", checked);
	}
	
	public static FilterVillagersSetting genericVision(boolean checked)
	{
		return new FilterVillagersSetting(
			"Won't show villagers and wandering traders.", checked);
	}
}
