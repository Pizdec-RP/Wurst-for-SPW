/*

 *



 */
package net.purefps.events;

import java.util.ArrayList;

import net.purefps.event.CancellableEvent;
import net.purefps.event.Listener;

public interface LeftClickListener extends Listener
{
	public void onLeftClick(LeftClickEvent event);
	
	public static class LeftClickEvent
		extends CancellableEvent<LeftClickListener>
	{
		@Override
		public void fire(ArrayList<LeftClickListener> listeners)
		{
			for(LeftClickListener listener : listeners)
			{
				listener.onLeftClick(this);
				
				if(isCancelled())
					break;
			}
		}
		
		@Override
		public Class<LeftClickListener> getListenerType()
		{
			return LeftClickListener.class;
		}
	}
}
