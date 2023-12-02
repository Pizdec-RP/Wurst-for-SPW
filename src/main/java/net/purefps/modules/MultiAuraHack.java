/*

 *



 */
package net.purefps.modules;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.PFPSClient;
import net.purefps.events.UpdateListener;
import net.purefps.module.Hack;
import net.purefps.settings.AttackSpeedSliderSetting;
import net.purefps.settings.PauseAttackOnContainersSetting;
import net.purefps.settings.SliderSetting;
import net.purefps.settings.SliderSetting.ValueDisplay;
import net.purefps.settings.filterlists.EntityFilterList;
import net.purefps.util.EntityUtils;
import net.purefps.util.RotationUtils;

@SearchTags({"multi aura", "ForceField", "force field"})
public final class MultiAuraHack extends Hack implements UpdateListener
{
	private final SliderSetting range =
		new SliderSetting("Range", 5, 1, 6, 0.05, ValueDisplay.DECIMAL);
	
	private final AttackSpeedSliderSetting speed =
		new AttackSpeedSliderSetting();
	
	private final SliderSetting fov =
		new SliderSetting("FOV", 360, 30, 360, 10, ValueDisplay.DEGREES);
	
	private final PauseAttackOnContainersSetting pauseOnContainers =
		new PauseAttackOnContainersSetting(false);
	
	private final EntityFilterList entityFilters =
		EntityFilterList.genericCombat();
	
	public MultiAuraHack()
	{
		super("MultiAura");
		setCategory(Category.COMBAT);
		
		addSetting(range);
		addSetting(speed);
		addSetting(fov);
		addSetting(pauseOnContainers);
		
		entityFilters.forEach(this::addSetting);
	}
	
	@Override
	public void onEnable()
	{
		// disable other killauras
		WURST.getHax().aimAssistHack.setEnabled(false);
		WURST.getHax().clickAuraHack.setEnabled(false);
		WURST.getHax().crystalAuraHack.setEnabled(false);
		WURST.getHax().fightBotHack.setEnabled(false);
		WURST.getHax().killauraLegitHack.setEnabled(false);
		WURST.getHax().killauraHack.setEnabled(false);
		WURST.getHax().protectHack.setEnabled(false);
		WURST.getHax().tpAuraHack.setEnabled(false);
		WURST.getHax().triggerBotHack.setEnabled(false);
		
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
		if(!speed.isTimeToAttack())
			return;
		
		if(pauseOnContainers.shouldPause())
			return;
		
		ClientPlayerEntity player = MC.player;
		
		// get entities
		Stream<Entity> stream = EntityUtils.getAttackableEntities();
		double rangeSq = Math.pow(range.getValue(), 2);
		stream = stream.filter(e -> MC.player.squaredDistanceTo(e) <= rangeSq);
		
		if(fov.getValue() < 360.0)
			stream = stream.filter(e -> RotationUtils.getAngleToLookVec(
				e.getBoundingBox().getCenter()) <= fov.getValue() / 2.0);
		
		stream = entityFilters.applyTo(stream);
		
		ArrayList<Entity> entities =
			stream.collect(Collectors.toCollection(ArrayList::new));
		if(entities.isEmpty())
			return;
		
		WURST.getHax().autoSwordHack.setSlot();
		
		// attack entities
		for(Entity entity : entities)
		{
			RotationUtils.Rotation rotations = RotationUtils
				.getNeededRotations(entity.getBoundingBox().getCenter());
			
			PFPSClient.MC.player.networkHandler.sendPacket(
				new PlayerMoveC2SPacket.LookAndOnGround(rotations.getYaw(),
					rotations.getPitch(), MC.player.isOnGround()));
			
			WURST.getHax().criticalsHack.doCritical();
			MC.interactionManager.attackEntity(player, entity);
		}
		
		player.swingHand(Hand.MAIN_HAND);
		speed.resetTimer();
	}
}
