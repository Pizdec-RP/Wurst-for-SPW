/*

 *



 */
package net.purefps.events;

import java.util.ArrayList;

import net.purefps.event.Event;
import net.purefps.event.Listener;

public interface PostMotionListener extends Listener
{
	public void onPostMotion();

	public static class PostMotionEvent extends Event<PostMotionListener>
	{
		public static final PostMotionEvent INSTANCE = new PostMotionEvent();

		@Override
		public void fire(ArrayList<PostMotionListener> listeners)
		{
			for(PostMotionListener listener : listeners)
				listener.onPostMotion();
		}

		@Override
		public Class<PostMotionListener> getListenerType()
		{
			return PostMotionListener.class;
		}
	}
}
