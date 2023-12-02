/*

 *



 */
package net.purefps.modules;

import java.util.ArrayDeque;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.events.PacketOutputListener;
import net.purefps.events.UpdateListener;
import net.purefps.module.DontSaveState;
import net.purefps.module.Hack;
import net.purefps.settings.SliderSetting;
import net.purefps.settings.SliderSetting.ValueDisplay;
import net.purefps.util.FakePlayerEntity;

@DontSaveState
@SearchTags({"LagSwitch", "lag switch"})
public final class BlinkHack extends Hack
	implements UpdateListener, PacketOutputListener
{
	private final SliderSetting limit = new SliderSetting("Limit",
		"Automatically restarts Blink once the given number of packets have been suspended.\n\n"
			+ "0 = no limit",
		0, 0, 500, 1, ValueDisplay.INTEGER.withLabel(0, "disabled"));
	
	private final ArrayDeque<PlayerMoveC2SPacket> packets = new ArrayDeque<>();
	private FakePlayerEntity fakePlayer;
	
	public BlinkHack()
	{
		super("Blink");
		setCategory(Category.MOVEMENT);
		addSetting(limit);
	}
	
	@Override
	public String getRenderName()
	{
		if(limit.getValueI() == 0)
			return getName() + " [" + packets.size() + "]";
		return getName() + " [" + packets.size() + "/" + limit.getValueI()
			+ "]";
	}
	
	@Override
	public void onEnable()
	{
		fakePlayer = new FakePlayerEntity();
		
		EVENTS.add(UpdateListener.class, this);
		EVENTS.add(PacketOutputListener.class, this);
	}
	
	@Override
	public void onDisable()
	{
		EVENTS.remove(UpdateListener.class, this);
		EVENTS.remove(PacketOutputListener.class, this);
		
		fakePlayer.despawn();
		packets.forEach(p -> MC.player.networkHandler.sendPacket(p));
		packets.clear();
	}
	
	@Override
	public void onUpdate()
	{
		if(limit.getValueI() == 0)
			return;
		
		if(packets.size() >= limit.getValueI())
		{
			setEnabled(false);
			setEnabled(true);
		}
	}
	
	@Override
	public void onSentPacket(PacketOutputEvent event)
	{
		if(!(event.getPacket() instanceof PlayerMoveC2SPacket))
			return;
		
		event.cancel();
		
		PlayerMoveC2SPacket packet = (PlayerMoveC2SPacket)event.getPacket();
		PlayerMoveC2SPacket prevPacket = packets.peekLast();
		
		if(prevPacket != null && packet.isOnGround() == prevPacket.isOnGround()
			&& packet.getYaw(-1) == prevPacket.getYaw(-1)
			&& packet.getPitch(-1) == prevPacket.getPitch(-1)
			&& packet.getX(-1) == prevPacket.getX(-1)
			&& packet.getY(-1) == prevPacket.getY(-1)
			&& packet.getZ(-1) == prevPacket.getZ(-1))
			return;
		
		packets.addLast(packet);
	}
	
	public void cancel()
	{
		packets.clear();
		fakePlayer.resetPlayerPosition();
		setEnabled(false);
	}
}
