/*

 *



 */
package net.purefps.events;

import java.util.ArrayList;
import java.util.Objects;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.purefps.event.Event;
import net.purefps.event.Listener;

public interface ShouldDrawSideListener extends Listener
{
	public void onShouldDrawSide(ShouldDrawSideEvent event);

	public static class ShouldDrawSideEvent
		extends Event<ShouldDrawSideListener>
	{
		private final BlockState state;
		private final BlockPos pos;
		private Boolean rendered;

		public ShouldDrawSideEvent(BlockState state, BlockPos pos)
		{
			this.state = Objects.requireNonNull(state);
			this.pos = pos;
		}

		public BlockState getState()
		{
			return state;
		}

		public BlockPos getPos()
		{
			return pos;
		}

		public Boolean isRendered()
		{
			return rendered;
		}

		public void setRendered(boolean rendered)
		{
			this.rendered = rendered;
		}

		@Override
		public void fire(ArrayList<ShouldDrawSideListener> listeners)
		{
			for(ShouldDrawSideListener listener : listeners)
				listener.onShouldDrawSide(this);
		}

		@Override
		public Class<ShouldDrawSideListener> getListenerType()
		{
			return ShouldDrawSideListener.class;
		}
	}
}
