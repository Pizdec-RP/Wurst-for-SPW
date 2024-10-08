/*

 *



 */
package net.purefps.commands;

import net.purefps.command.CmdException;
import net.purefps.command.CmdSyntaxError;
import net.purefps.command.Command;

public final class LeaveCmd extends Command
{
	public LeaveCmd()
	{
		super("leave", "Instantly disconnects from the server.", ".leave");
	}

	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length == 1 && args[0].equalsIgnoreCase("taco"))
			for(int i = 0; i < 128; i++)
				MC.getNetworkHandler().sendChatMessage("Taco!");
		else if(args.length != 0)
			throw new CmdSyntaxError();

		MC.world.disconnect();
	}

	@Override
	public String getPrimaryAction()
	{
		return "Leave";
	}

	@Override
	public void doPrimaryAction()
	{
		WURST.getCmdProcessor().process("leave");
	}
}
