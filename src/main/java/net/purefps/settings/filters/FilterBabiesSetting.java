/*

 *



 */
package net.purefps.settings.filters;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TadpoleEntity;

public final class FilterBabiesSetting extends EntityFilterCheckbox
{
	private static final String EXCEPTIONS_TEXT = "\n\nThis filter does not"
		+ " affect baby zombies and other hostile baby mobs.";

	public FilterBabiesSetting(String description, boolean checked)
	{
		super("Filter babies", description + EXCEPTIONS_TEXT, checked);
	}

	@Override
	public boolean test(Entity e)
	{
		// filter out passive entity babies
		// filter out tadpoles
		if((e instanceof PassiveEntity pe && pe.isBaby()) || (e instanceof TadpoleEntity))
			return false;

		return true;
	}

	public static FilterBabiesSetting genericCombat(boolean checked)
	{
		return new FilterBabiesSetting(
			"Won't attack baby pigs, baby villagers, etc.", checked);
	}
}
