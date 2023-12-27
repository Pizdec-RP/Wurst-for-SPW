/*

 *



 */
package net.purefps.commands;

import net.purefps.command.CmdException;
import net.purefps.command.CmdSyntaxError;
import net.purefps.command.Command;

public final class ClearCmd extends Command
{
	public ClearCmd()
	{
		super("clear", "Clears the chat completely.", ".clear");
	}

	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length > 0)
			throw new CmdSyntaxError();

		MC.inGameHud.getChatHud().clear(true);
	}
}
