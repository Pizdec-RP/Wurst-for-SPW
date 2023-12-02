/*

 *



 */
package net.purefps.events;

import java.util.ArrayList;

import net.purefps.event.Event;
import net.purefps.event.Listener;

public interface MouseScrollListener extends Listener
{
	public void onMouseScroll(double amount);
	
	public static class MouseScrollEvent extends Event<MouseScrollListener>
	{
		private final double amount;
		
		public MouseScrollEvent(double amount)
		{
			this.amount = amount;
		}
		
		@Override
		public void fire(ArrayList<MouseScrollListener> listeners)
		{
			for(MouseScrollListener listener : listeners)
				listener.onMouseScroll(amount);
		}
		
		@Override
		public Class<MouseScrollListener> getListenerType()
		{
			return MouseScrollListener.class;
		}
	}
}
