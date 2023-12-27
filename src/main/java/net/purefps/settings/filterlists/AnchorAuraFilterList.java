/*

 *



 */
package net.purefps.settings.filterlists;

import java.util.ArrayList;
import java.util.List;

import net.purefps.settings.filters.AttackDetectingEntityFilter;
import net.purefps.settings.filters.FilterAllaysSetting;
import net.purefps.settings.filters.FilterArmorStandsSetting;
import net.purefps.settings.filters.FilterBatsSetting;
import net.purefps.settings.filters.FilterGolemsSetting;
import net.purefps.settings.filters.FilterHostileSetting;
import net.purefps.settings.filters.FilterInvisibleSetting;
import net.purefps.settings.filters.FilterNamedSetting;
import net.purefps.settings.filters.FilterNeutralSetting;
import net.purefps.settings.filters.FilterPassiveSetting;
import net.purefps.settings.filters.FilterPassiveWaterSetting;
import net.purefps.settings.filters.FilterPiglinsSetting;
import net.purefps.settings.filters.FilterPlayersSetting;
import net.purefps.settings.filters.FilterShulkersSetting;
import net.purefps.settings.filters.FilterSlimesSetting;
import net.purefps.settings.filters.FilterVillagersSetting;
import net.purefps.settings.filters.FilterZombiePiglinsSetting;
import net.purefps.settings.filters.FilterZombieVillagersSetting;

public final class AnchorAuraFilterList extends EntityFilterList
{
	private AnchorAuraFilterList(List<EntityFilter> filters)
	{
		super(filters);
	}

	public static AnchorAuraFilterList create()
	{
		ArrayList<EntityFilter> builder = new ArrayList<>();
		String damageWarning =
			"\n\nThey can still take damage if they get too close to a valid target or an existing anchor.";

		builder.add(new FilterPlayersSetting(
			"Won't target other players when auto-placing anchors."
				+ damageWarning,
			false));

		builder.add(new FilterHostileSetting("Won't target hostile mobs like"
			+ " zombies and creepers when auto-placing anchors."
			+ damageWarning, true));

		builder.add(new FilterNeutralSetting("Won't target neutral mobs like"
			+ " endermen and wolves when auto-placing anchors." + damageWarning,
			AttackDetectingEntityFilter.Mode.ON));

		builder.add(new FilterPassiveSetting("Won't target animals like pigs"
			+ " and cows, ambient mobs like bats, and water mobs like fish,"
			+ " squid and dolphins when auto-placing anchors." + damageWarning,
			true));

		builder.add(new FilterPassiveWaterSetting("Won't target passive water"
			+ " mobs like fish, squid, dolphins and axolotls when auto-placing"
			+ " anchors." + damageWarning, true));

		builder.add(new FilterBatsSetting("Won't target bats and any other"
			+ " \"ambient\" mobs when auto-placing anchors." + damageWarning,
			true));

		builder.add(new FilterSlimesSetting("Won't target slimes when"
			+ " auto-placing anchors." + damageWarning, true));

		builder.add(new FilterVillagersSetting("Won't target villagers and"
			+ " wandering traders when auto-placing anchors." + damageWarning,
			true));

		builder.add(new FilterZombieVillagersSetting("Won't target zombified"
			+ " villagers when auto-placing anchors." + damageWarning, true));

		builder.add(new FilterGolemsSetting("Won't target iron golems and snow"
			+ " golems when auto-placing anchors." + damageWarning, true));

		builder.add(new FilterPiglinsSetting(
			"Won't target piglins when auto-placing anchors.",
			AttackDetectingEntityFilter.Mode.ON));

		builder.add(new FilterZombiePiglinsSetting("Won't target"
			+ " zombified piglins when auto-placing anchors." + damageWarning,
			AttackDetectingEntityFilter.Mode.ON));

		builder.add(new FilterShulkersSetting("Won't target shulkers when"
			+ " auto-placing anchors." + damageWarning, true));

		builder.add(new FilterAllaysSetting(
			"Won't target allays when auto-placing anchors." + damageWarning,
			true));

		builder.add(new FilterInvisibleSetting(
			"Won't target invisible entities when auto-placing anchors."
				+ damageWarning,
			false));

		builder.add(new FilterNamedSetting(
			"Won't target name-tagged entities when auto-placing anchors."
				+ damageWarning,
			false));

		builder.add(new FilterArmorStandsSetting(
			"Won't target armor stands when auto-placing anchors."
				+ damageWarning,
			true));

		return new AnchorAuraFilterList(builder);
	}
}
