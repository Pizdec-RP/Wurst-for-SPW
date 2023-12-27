/*

 *



 */
package net.purefps.commands;

import net.purefps.command.CmdException;
import net.purefps.command.CmdSyntaxError;
import net.purefps.command.Command;
import net.purefps.modules.RemoteViewHack;

public final class RvCmd extends Command
{
	public RvCmd()
	{
		super("rv", "Makes RemoteView target a specific entity.",
			".rv <entity>");
	}

	@Override
	public void call(String[] args) throws CmdException
	{
		RemoteViewHack remoteView = WURST.getHax().remoteViewHack;

		if(args.length != 1)
			throw new CmdSyntaxError();

		remoteView.onToggledByCommand(args[0]);
	}
}
