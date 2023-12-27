/*

 *



 */
package net.purefps.settings.filters;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MagmaCubeEntity;
import net.minecraft.entity.mob.SlimeEntity;

public final class FilterSlimesSetting extends EntityFilterCheckbox
{
	private static final String EXCEPTIONS_TEXT =
		"\n\nThis filter does not affect magma cubes.";

	public FilterSlimesSetting(String description, boolean checked)
	{
		super("Filter slimes", description + EXCEPTIONS_TEXT, checked);
	}

	@Override
	public boolean test(Entity e)
	{
		return !(e instanceof SlimeEntity) || e instanceof MagmaCubeEntity;
	}

	public static FilterSlimesSetting genericCombat(boolean checked)
	{
		return new FilterSlimesSetting("Won't attack slimes.", checked);
	}

	public static FilterSlimesSetting genericVision(boolean checked)
	{
		return new FilterSlimesSetting("Won't show slimes.", checked);
	}
}
