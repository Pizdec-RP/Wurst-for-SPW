/*

 *



 */
package net.purefps.mixinterface;

import net.minecraft.client.util.Session;

public interface IMinecraftClient
{
	public IClientPlayerInteractionManager getInteractionManager();
	
	public IClientPlayerEntity getPlayer();
	
	public void setSession(Session session);
}
