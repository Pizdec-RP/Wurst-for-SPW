/*

 *



 */
package net.purefps.events;

import java.util.ArrayList;

import net.purefps.event.Event;
import net.purefps.event.Listener;

public interface PreMotionListener extends Listener
{
	public void onPreMotion();

	public static class PreMotionEvent extends Event<PreMotionListener>
	{
		public static final PreMotionEvent INSTANCE = new PreMotionEvent();

		@Override
		public void fire(ArrayList<PreMotionListener> listeners)
		{
			for(PreMotionListener listener : listeners)
				listener.onPreMotion();
		}

		@Override
		public Class<PreMotionListener> getListenerType()
		{
			return PreMotionListener.class;
		}
	}
}
