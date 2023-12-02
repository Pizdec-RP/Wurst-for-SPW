/*

 *



 */
package net.purefps.events;

import java.util.ArrayList;

import net.purefps.event.CancellableEvent;
import net.purefps.event.Listener;

public interface IsNormalCubeListener extends Listener
{
	public void onIsNormalCube(IsNormalCubeEvent event);
	
	public static class IsNormalCubeEvent
		extends CancellableEvent<IsNormalCubeListener>
	{
		@Override
		public void fire(ArrayList<IsNormalCubeListener> listeners)
		{
			for(IsNormalCubeListener listener : listeners)
			{
				listener.onIsNormalCube(this);
				
				if(isCancelled())
					break;
			}
		}
		
		@Override
		public Class<IsNormalCubeListener> getListenerType()
		{
			return IsNormalCubeListener.class;
		}
	}
}
