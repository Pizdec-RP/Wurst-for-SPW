/*

 *



 */
package net.purefps.events;

import java.util.ArrayList;

import net.purefps.event.Event;
import net.purefps.event.Listener;

public interface PlayerMoveListener extends Listener
{
	public void onPlayerMove();

	public static class PlayerMoveEvent extends Event<PlayerMoveListener>
	{
		public static final PlayerMoveEvent INSTANCE = new PlayerMoveEvent();

		@Override
		public void fire(ArrayList<PlayerMoveListener> listeners)
		{
			for(PlayerMoveListener listener : listeners)
				listener.onPlayerMove();
		}

		@Override
		public Class<PlayerMoveListener> getListenerType()
		{
			return PlayerMoveListener.class;
		}
	}
}
