/*

 *



 */
package net.purefps.modules;

import net.minecraft.entity.Entity;
import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.module.Hack;
import net.purefps.settings.filterlists.EntityFilterList;
import net.purefps.settings.filters.*;

@SearchTags({"true sight"})
public final class TrueSightHack extends Hack
{
	private final EntityFilterList entityFilters =
		new EntityFilterList(FilterHostileSetting.genericVision(false),
			FilterNeutralSetting
				.genericVision(AttackDetectingEntityFilter.Mode.OFF),
			FilterPassiveSetting.genericVision(false),
			FilterPassiveWaterSetting.genericVision(false),
			FilterBatsSetting.genericVision(false),
			FilterSlimesSetting.genericVision(false),
			FilterPetsSetting.genericVision(false),
			FilterVillagersSetting.genericVision(false),
			FilterZombieVillagersSetting.genericVision(false),
			FilterGolemsSetting.genericVision(false),
			FilterPiglinsSetting
				.genericVision(AttackDetectingEntityFilter.Mode.OFF),
			FilterZombiePiglinsSetting
				.genericVision(AttackDetectingEntityFilter.Mode.OFF),
			FilterEndermenSetting
				.genericVision(AttackDetectingEntityFilter.Mode.OFF),
			FilterShulkersSetting.genericVision(false),
			FilterAllaysSetting.genericVision(false),
			FilterNamedSetting.genericVision(false),
			FilterArmorStandsSetting.genericVision(false));
	
	public TrueSightHack()
	{
		super("TrueSight");
		setCategory(Category.RENDER);
		entityFilters.forEach(this::addSetting);
	}
	
	public boolean shouldBeVisible(Entity entity)
	{
		return isEnabled() && entityFilters.testOne(entity);
	}
	
	// See EntityMixin.onIsInvisibleTo()
}
