/*

 *



 */
package net.purefps.events;

import java.util.ArrayList;

import net.purefps.event.Event;
import net.purefps.event.Listener;

public interface HitResultRayTraceListener extends Listener
{
	public void onHitResultRayTrace(float partialTicks);
	
	public static class HitResultRayTraceEvent
		extends Event<HitResultRayTraceListener>
	{
		private float partialTicks;
		
		public HitResultRayTraceEvent(float partialTicks)
		{
			this.partialTicks = partialTicks;
		}
		
		@Override
		public void fire(ArrayList<HitResultRayTraceListener> listeners)
		{
			for(HitResultRayTraceListener listener : listeners)
				listener.onHitResultRayTrace(partialTicks);
		}
		
		@Override
		public Class<HitResultRayTraceListener> getListenerType()
		{
			return HitResultRayTraceListener.class;
		}
	}
}
