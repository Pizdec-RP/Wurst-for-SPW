/*

 *



 */
package net.purefps.modules;

import java.util.Random;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.events.UpdateListener;
import net.purefps.module.Hack;

@SearchTags({"Retarded"})
public final class DerpHack extends Hack implements UpdateListener
{
	private final Random random = new Random();

	public DerpHack()
	{
		super("Derp");
		setCategory(Category.FUN);
	}

	@Override
	public void onEnable()
	{
		// disable incompatible derps
		WURST.getHax().headRollHack.setEnabled(false);
		WURST.getHax().tiredHack.setEnabled(false);

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
		float yaw = MC.player.getYaw() + random.nextFloat() * 360F - 180F;
		float pitch = random.nextFloat() * 180F - 90F;

		MC.player.networkHandler
			.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(yaw, pitch,
				MC.player.isOnGround()));
	}
}
