/*

 *



 */
package net.purefps.modules;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.events.PacketOutputListener;
import net.purefps.module.Hack;

@SearchTags({"potion saver"})
public final class PotionSaverHack extends Hack implements PacketOutputListener
{
	public PotionSaverHack()
	{
		super("PotionSaver");
		setCategory(Category.OTHER);
	}

	@Override
	protected void onEnable()
	{
		EVENTS.add(PacketOutputListener.class, this);
	}

	@Override
	protected void onDisable()
	{
		EVENTS.remove(PacketOutputListener.class, this);
	}

	@Override
	public void onSentPacket(PacketOutputEvent event)
	{
		if(!isFrozen())
			return;

		if(event.getPacket() instanceof PlayerMoveC2SPacket)
			event.cancel();
	}

	public boolean isFrozen()
	{
		return isEnabled() && MC.player != null
			&& !MC.player.getActiveStatusEffects().isEmpty()
			&& MC.player.getVelocity().x == 0 && MC.player.getVelocity().z == 0;
	}
}
