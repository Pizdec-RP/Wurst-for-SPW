/*

 *



 */
package net.purefps.events;

import java.util.ArrayList;

import net.purefps.event.CancellableEvent;
import net.purefps.event.Listener;

public interface SetOpaqueCubeListener extends Listener
{
	public void onSetOpaqueCube(SetOpaqueCubeEvent event);
	
	public static class SetOpaqueCubeEvent
		extends CancellableEvent<SetOpaqueCubeListener>
	{
		@Override
		public void fire(ArrayList<SetOpaqueCubeListener> listeners)
		{
			for(SetOpaqueCubeListener listener : listeners)
			{
				listener.onSetOpaqueCube(this);
				
				if(isCancelled())
					break;
			}
		}
		
		@Override
		public Class<SetOpaqueCubeListener> getListenerType()
		{
			return SetOpaqueCubeListener.class;
		}
	}
}
