/*

 *



 */
package net.purefps.commands;

import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.purefps.command.CmdException;
import net.purefps.command.CmdSyntaxError;
import net.purefps.command.Command;
import net.purefps.events.RenderListener;
import net.purefps.util.ChatUtils;

public final class InvseeCmd extends Command implements RenderListener
{
	private String targetName;
	
	public InvseeCmd()
	{
		super("invsee",
			"Allows you to see parts of another player's inventory.",
			".invsee <player>");
	}
	
	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length != 1)
			throw new CmdSyntaxError();
		
		if(MC.player.getAbilities().creativeMode)
		{
			ChatUtils.error("Survival mode only.");
			return;
		}
		
		targetName = args[0];
		EVENTS.add(RenderListener.class, this);
	}
	
	@Override
	public void onRender(MatrixStack matrixStack, float partialTicks)
	{
		boolean found = false;
		
		for(Entity entity : MC.world.getEntities())
		{
			if(!(entity instanceof OtherClientPlayerEntity))
				continue;
			
			OtherClientPlayerEntity player = (OtherClientPlayerEntity)entity;
			
			String otherPlayerName = player.getName().getString();
			if(!otherPlayerName.equalsIgnoreCase(targetName))
				continue;
			
			ChatUtils.message("Showing inventory of " + otherPlayerName + ".");
			MC.setScreen(new InventoryScreen(player));
			found = true;
			break;
		}
		
		if(!found)
			ChatUtils.error("Player not found.");
		
		targetName = null;
		EVENTS.remove(RenderListener.class, this);
	}
}
