/*

 *



 */
package net.purefps.events;

import java.util.ArrayList;

import net.purefps.event.Event;
import net.purefps.event.Listener;

public interface IsPlayerInLavaListener extends Listener
{
	public void onIsPlayerInLava(IsPlayerInLavaEvent event);
	
	public static class IsPlayerInLavaEvent
		extends Event<IsPlayerInLavaListener>
	{
		private boolean inLava;
		private final boolean normallyInLava;
		
		public IsPlayerInLavaEvent(boolean inLava)
		{
			this.inLava = inLava;
			normallyInLava = inLava;
		}
		
		public boolean isInLava()
		{
			return inLava;
		}
		
		public void setInLava(boolean inLava)
		{
			this.inLava = inLava;
		}
		
		public boolean isNormallyInLava()
		{
			return normallyInLava;
		}
		
		@Override
		public void fire(ArrayList<IsPlayerInLavaListener> listeners)
		{
			for(IsPlayerInLavaListener listener : listeners)
				listener.onIsPlayerInLava(this);
		}
		
		@Override
		public Class<IsPlayerInLavaListener> getListenerType()
		{
			return IsPlayerInLavaListener.class;
		}
	}
}
