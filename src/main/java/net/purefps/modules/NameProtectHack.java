/*

 *



 */
package net.purefps.modules;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.module.Hack;

@SearchTags({"name protect"})
public final class NameProtectHack extends Hack
{
	public NameProtectHack()
	{
		super("NameProtect");
		setCategory(Category.RENDER);
	}
	
	public String protect(String string)
	{
		if(!isEnabled() || MC.player == null)
			return string;
		
		String me = MC.getSession().getUsername();
		if(string.contains(me))
			return string.replace(me, "\u00a7oMe\u00a7r");
		
		int i = 0;
		for(PlayerListEntry info : MC.player.networkHandler.getPlayerList())
		{
			i++;
			String name =
				info.getProfile().getName().replaceAll("\u00a7(?:\\w|\\d)", "");
			
			if(string.contains(name))
				return string.replace(name, "\u00a7oPlayer" + i + "\u00a7r");
		}
		
		for(AbstractClientPlayerEntity player : MC.world.getPlayers())
		{
			i++;
			String name = player.getName().getString();
			
			if(string.contains(name))
				return string.replace(name, "\u00a7oPlayer" + i + "\u00a7r");
		}
		
		return string;
	}
}
