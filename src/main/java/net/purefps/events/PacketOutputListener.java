/*

 *



 */
package net.purefps.events;

import java.util.ArrayList;

import net.minecraft.network.packet.Packet;
import net.purefps.event.CancellableEvent;
import net.purefps.event.Listener;

public interface PacketOutputListener extends Listener
{
	public void onSentPacket(PacketOutputEvent event);
	
	public static class PacketOutputEvent
		extends CancellableEvent<PacketOutputListener>
	{
		private Packet<?> packet;
		
		public PacketOutputEvent(Packet<?> packet)
		{
			this.packet = packet;
		}
		
		public Packet<?> getPacket()
		{
			return packet;
		}
		
		public void setPacket(Packet<?> packet)
		{
			this.packet = packet;
		}
		
		@Override
		public void fire(ArrayList<PacketOutputListener> listeners)
		{
			for(PacketOutputListener listener : listeners)
			{
				listener.onSentPacket(this);
				
				if(isCancelled())
					break;
			}
		}
		
		@Override
		public Class<PacketOutputListener> getListenerType()
		{
			return PacketOutputListener.class;
		}
	}
}
