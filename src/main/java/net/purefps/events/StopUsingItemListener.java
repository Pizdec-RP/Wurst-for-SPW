/*

 *



 */
package net.purefps.events;

import java.util.ArrayList;

import net.purefps.event.Event;
import net.purefps.event.Listener;

public interface StopUsingItemListener extends Listener
{
	public void onStopUsingItem();
	
	public static class StopUsingItemEvent extends Event<StopUsingItemListener>
	{
		public static final StopUsingItemEvent INSTANCE =
			new StopUsingItemEvent();
		
		@Override
		public void fire(ArrayList<StopUsingItemListener> listeners)
		{
			for(StopUsingItemListener listener : listeners)
				listener.onStopUsingItem();
		}
		
		@Override
		public Class<StopUsingItemListener> getListenerType()
		{
			return StopUsingItemListener.class;
		}
	}
}
