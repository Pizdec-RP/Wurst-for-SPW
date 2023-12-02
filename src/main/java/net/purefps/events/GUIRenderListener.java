/*

 *



 */
package net.purefps.events;

import java.util.ArrayList;

import net.minecraft.client.gui.DrawContext;
import net.purefps.event.Event;
import net.purefps.event.Listener;

public interface GUIRenderListener extends Listener
{
	public void onRenderGUI(DrawContext context, float partialTicks);
	
	public static class GUIRenderEvent extends Event<GUIRenderListener>
	{
		private final float partialTicks;
		private final DrawContext context;
		
		public GUIRenderEvent(DrawContext context, float partialTicks)
		{
			this.context = context;
			this.partialTicks = partialTicks;
		}
		
		@Override
		public void fire(ArrayList<GUIRenderListener> listeners)
		{
			for(GUIRenderListener listener : listeners)
				listener.onRenderGUI(context, partialTicks);
		}
		
		@Override
		public Class<GUIRenderListener> getListenerType()
		{
			return GUIRenderListener.class;
		}
	}
}
