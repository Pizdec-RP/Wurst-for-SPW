/*

 *



 */
package net.purefps.events;

import java.util.ArrayList;

import net.purefps.event.CancellableEvent;
import net.purefps.event.Listener;

public interface CameraTransformViewBobbingListener extends Listener
{
	public void onCameraTransformViewBobbing(
		CameraTransformViewBobbingEvent event);
	
	public static class CameraTransformViewBobbingEvent
		extends CancellableEvent<CameraTransformViewBobbingListener>
	{
		@Override
		public void fire(
			ArrayList<CameraTransformViewBobbingListener> listeners)
		{
			for(CameraTransformViewBobbingListener listener : listeners)
			{
				listener.onCameraTransformViewBobbing(this);
				
				if(isCancelled())
					break;
			}
		}
		
		@Override
		public Class<CameraTransformViewBobbingListener> getListenerType()
		{
			return CameraTransformViewBobbingListener.class;
		}
	}
}
