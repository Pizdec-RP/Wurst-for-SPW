/*

 *



 */
package net.purefps.events;

import java.util.ArrayList;

import net.minecraft.util.shape.VoxelShape;
import net.purefps.event.Event;
import net.purefps.event.Listener;

public interface CactusCollisionShapeListener extends Listener
{
	public void onCactusCollisionShape(CactusCollisionShapeEvent event);

	public static class CactusCollisionShapeEvent
		extends Event<CactusCollisionShapeListener>
	{
		private VoxelShape collisionShape;

		public VoxelShape getCollisionShape()
		{
			return collisionShape;
		}

		public void setCollisionShape(VoxelShape collisionShape)
		{
			this.collisionShape = collisionShape;
		}

		@Override
		public void fire(ArrayList<CactusCollisionShapeListener> listeners)
		{
			for(CactusCollisionShapeListener listener : listeners)
				listener.onCactusCollisionShape(this);
		}

		@Override
		public Class<CactusCollisionShapeListener> getListenerType()
		{
			return CactusCollisionShapeListener.class;
		}
	}
}
