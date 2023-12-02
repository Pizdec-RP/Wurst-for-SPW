/*

 *



 */
package net.purefps.events;

import java.util.ArrayList;

import net.purefps.event.Event;
import net.purefps.event.Listener;

public interface DeathListener extends Listener
{
	public void onDeath();
	
	public static class DeathEvent extends Event<DeathListener>
	{
		public static final DeathEvent INSTANCE = new DeathEvent();
		
		@Override
		public void fire(ArrayList<DeathListener> listeners)
		{
			for(DeathListener listener : listeners)
				listener.onDeath();
		}
		
		@Override
		public Class<DeathListener> getListenerType()
		{
			return DeathListener.class;
		}
	}
}
