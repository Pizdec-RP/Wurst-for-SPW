/*

 *



 */
package net.purefps.modules;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.events.UpdateListener;
import net.purefps.module.Hack;
import net.purefps.settings.AttackSpeedSliderSetting;
import net.purefps.settings.CheckboxSetting;
import net.purefps.settings.SliderSetting;
import net.purefps.settings.SliderSetting.ValueDisplay;
import net.purefps.settings.filterlists.EntityFilterList;
import net.purefps.util.EntityUtils;

@SearchTags({"trigger bot"})
public final class TriggerBotHack extends Hack implements UpdateListener
{
	private final SliderSetting range =
		new SliderSetting("Range", 4.25, 1, 6, 0.05, ValueDisplay.DECIMAL);

	private final AttackSpeedSliderSetting speed =
		new AttackSpeedSliderSetting();

	private final CheckboxSetting attackWhileBlocking = new CheckboxSetting(
		"Attack while blocking",
		"Whether or not to attack while blocking with a shield / using items.",
		false);

	private final EntityFilterList entityFilters =
		EntityFilterList.genericCombat();

	public TriggerBotHack()
	{
		super("TriggerBot");
		setCategory(Category.COMBAT);

		addSetting(range);
		addSetting(speed);
		addSetting(attackWhileBlocking);

		entityFilters.forEach(this::addSetting);
	}

	@Override
	public void onEnable()
	{
		// disable other killauras
		WURST.getHax().clickAuraHack.setEnabled(false);
		WURST.getHax().crystalAuraHack.setEnabled(false);
		WURST.getHax().fightBotHack.setEnabled(false);
		WURST.getHax().killauraLegitHack.setEnabled(false);
		WURST.getHax().killauraHack.setEnabled(false);
		WURST.getHax().multiAuraHack.setEnabled(false);
		WURST.getHax().protectHack.setEnabled(false);
		WURST.getHax().tpAuraHack.setEnabled(false);

		speed.resetTimer();
		EVENTS.add(UpdateListener.class, this);
	}

	@Override
	public void onDisable()
	{
		EVENTS.remove(UpdateListener.class, this);
	}

	@Override
	public void onUpdate()
	{
		speed.updateTimer();
		// don't attack when a container/inventory screen is open
		if(!speed.isTimeToAttack() || (MC.currentScreen instanceof HandledScreen))
			return;

		ClientPlayerEntity player = MC.player;
		if(player.isUsingItem() && !attackWhileBlocking.isChecked())
			return;

		if(MC.crosshairTarget == null
			|| !(MC.crosshairTarget instanceof EntityHitResult))
			return;

		Entity target = ((EntityHitResult)MC.crosshairTarget).getEntity();
		if(!isCorrectEntity(target))
			return;

		WURST.getHax().autoSwordHack.setSlot();

		WURST.getHax().criticalsHack.doCritical();
		MC.interactionManager.attackEntity(player, target);
		player.swingHand(Hand.MAIN_HAND);
		speed.resetTimer();
	}

	private boolean isCorrectEntity(Entity entity)
	{
		if(!EntityUtils.IS_ATTACKABLE.test(entity) || (MC.player.squaredDistanceTo(entity) > range.getValueSq()))
			return false;

		return entityFilters.testOne(entity);
	}
}
