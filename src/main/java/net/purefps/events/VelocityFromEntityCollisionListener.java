/*

 *



 */
package net.purefps.events;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.purefps.event.CancellableEvent;
import net.purefps.event.Listener;

public interface VelocityFromEntityCollisionListener extends Listener
{
	public void onVelocityFromEntityCollision(
		VelocityFromEntityCollisionEvent event);
	
	public static class VelocityFromEntityCollisionEvent
		extends CancellableEvent<VelocityFromEntityCollisionListener>
	{
		private final Entity entity;
		
		public VelocityFromEntityCollisionEvent(Entity entity)
		{
			this.entity = entity;
		}
		
		public Entity getEntity()
		{
			return entity;
		}
		
		@Override
		public void fire(
			ArrayList<VelocityFromEntityCollisionListener> listeners)
		{
			for(VelocityFromEntityCollisionListener listener : listeners)
			{
				listener.onVelocityFromEntityCollision(this);
				
				if(isCancelled())
					break;
			}
		}
		
		@Override
		public Class<VelocityFromEntityCollisionListener> getListenerType()
		{
			return VelocityFromEntityCollisionListener.class;
		}
	}
}
