/*

 *



 */
package net.purefps.settings.filters;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.passive.GolemEntity;

public final class FilterGolemsSetting extends EntityFilterCheckbox
{
	public FilterGolemsSetting(String description, boolean checked)
	{
		super("Filter golems", description, checked);
	}

	@Override
	public boolean test(Entity e)
	{
		return !(e instanceof GolemEntity) || e instanceof ShulkerEntity;
	}

	public static FilterGolemsSetting genericCombat(boolean checked)
	{
		return new FilterGolemsSetting(
			"Won't attack iron golems and snow golems.", checked);
	}

	public static FilterGolemsSetting genericVision(boolean checked)
	{
		return new FilterGolemsSetting(
			"Won't show iron golems and snow golems.", checked);
	}
}
