/*

 *



 */
package net.purefps.settings.filterlists;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import net.minecraft.entity.Entity;
import net.purefps.settings.Setting;
import net.purefps.settings.filters.AttackDetectingEntityFilter;
import net.purefps.settings.filters.FilterAllaysSetting;
import net.purefps.settings.filters.FilterArmorStandsSetting;
import net.purefps.settings.filters.FilterBabiesSetting;
import net.purefps.settings.filters.FilterBatsSetting;
import net.purefps.settings.filters.FilterCrystalsSetting;
import net.purefps.settings.filters.FilterEndermenSetting;
import net.purefps.settings.filters.FilterFlyingSetting;
import net.purefps.settings.filters.FilterGolemsSetting;
import net.purefps.settings.filters.FilterHostileSetting;
import net.purefps.settings.filters.FilterInvisibleSetting;
import net.purefps.settings.filters.FilterNamedSetting;
import net.purefps.settings.filters.FilterNeutralSetting;
import net.purefps.settings.filters.FilterPassiveSetting;
import net.purefps.settings.filters.FilterPassiveWaterSetting;
import net.purefps.settings.filters.FilterPetsSetting;
import net.purefps.settings.filters.FilterPiglinsSetting;
import net.purefps.settings.filters.FilterPlayersSetting;
import net.purefps.settings.filters.FilterShulkerBulletSetting;
import net.purefps.settings.filters.FilterShulkersSetting;
import net.purefps.settings.filters.FilterSleepingSetting;
import net.purefps.settings.filters.FilterSlimesSetting;
import net.purefps.settings.filters.FilterVillagersSetting;
import net.purefps.settings.filters.FilterZombiePiglinsSetting;
import net.purefps.settings.filters.FilterZombieVillagersSetting;

public class EntityFilterList
{
	private final List<EntityFilter> entityFilters;

	public EntityFilterList(EntityFilter... filters)
	{
		this(Arrays.asList(filters));
	}

	public EntityFilterList(List<EntityFilter> filters)
	{
		entityFilters = Collections.unmodifiableList(filters);
	}

	public final void forEach(Consumer<? super Setting> action)
	{
		entityFilters.stream().map(EntityFilter::getSetting).forEach(action);
	}

	public final <T extends Entity> Stream<T> applyTo(Stream<T> stream)
	{
		for(EntityFilter filter : entityFilters)
		{
			if(!filter.isFilterEnabled())
				continue;

			stream = stream.filter(filter);
		}

		return stream;
	}

	public final boolean testOne(Entity entity)
	{
		for(EntityFilter filter : entityFilters)
			if(filter.isFilterEnabled() && !filter.test(entity))
				return false;

		return true;
	}

	public static EntityFilterList genericCombat()
	{
		return new EntityFilterList(FilterPlayersSetting.genericCombat(false),
			FilterSleepingSetting.genericCombat(false),
			FilterFlyingSetting.genericCombat(0),
			FilterHostileSetting.genericCombat(false),
			FilterNeutralSetting
				.genericCombat(AttackDetectingEntityFilter.Mode.OFF),
			FilterPassiveSetting.genericCombat(false),
			FilterPassiveWaterSetting.genericCombat(false),
			FilterBabiesSetting.genericCombat(false),
			FilterBatsSetting.genericCombat(false),
			FilterSlimesSetting.genericCombat(false),
			FilterPetsSetting.genericCombat(false),
			FilterVillagersSetting.genericCombat(false),
			FilterZombieVillagersSetting.genericCombat(false),
			FilterGolemsSetting.genericCombat(false),
			FilterPiglinsSetting
				.genericCombat(AttackDetectingEntityFilter.Mode.OFF),
			FilterZombiePiglinsSetting
				.genericCombat(AttackDetectingEntityFilter.Mode.OFF),
			FilterEndermenSetting
				.genericCombat(AttackDetectingEntityFilter.Mode.OFF),
			FilterShulkersSetting.genericCombat(false),
			FilterAllaysSetting.genericCombat(false),
			FilterInvisibleSetting.genericCombat(false),
			FilterNamedSetting.genericCombat(false),
			FilterShulkerBulletSetting.genericCombat(false),
			FilterArmorStandsSetting.genericCombat(false),
			FilterCrystalsSetting.genericCombat(false));
	}

	public static interface EntityFilter extends Predicate<Entity>
	{
		public boolean isFilterEnabled();

		public Setting getSetting();
	}
}
