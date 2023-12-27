/*

 *



 */
package net.purefps.events;

import java.util.ArrayList;

import net.minecraft.client.MinecraftClient;
import net.purefps.event.CancellableEvent;
import net.purefps.event.Listener;

public interface RightClickListener extends Listener
{
	/**
	 * Fired in {@link MinecraftClient#doItemUse()} after the
	 * {@code interactionManager.isBreakingBlock()} check, but before the
	 * item use cooldown is increased.
	 */
	public void onRightClick(RightClickEvent event);

	public static class RightClickEvent
		extends CancellableEvent<RightClickListener>
	{
		@Override
		public void fire(ArrayList<RightClickListener> listeners)
		{
			for(RightClickListener listener : listeners)
			{
				listener.onRightClick(this);

				if(isCancelled())
					break;
			}
		}

		@Override
		public Class<RightClickListener> getListenerType()
		{
			return RightClickListener.class;
		}
	}
}
