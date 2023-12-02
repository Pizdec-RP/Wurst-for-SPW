/*

 *



 */
package net.purefps.modules;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.purefps.Category;
import net.purefps.events.UpdateListener;
import net.purefps.module.Hack;

public final class TiredHack extends Hack implements UpdateListener
{
	public TiredHack()
	{
		super("Tired");
		setCategory(Category.FUN);
	}
	
	@Override
	public void onEnable()
	{
		// disable incompatible derps
		WURST.getHax().derpHack.setEnabled(false);
		WURST.getHax().headRollHack.setEnabled(false);
		
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
		MC.player.networkHandler.sendPacket(
			new PlayerMoveC2SPacket.LookAndOnGround(MC.player.getYaw(),
				MC.player.age % 100, MC.player.isOnGround()));
	}
}
