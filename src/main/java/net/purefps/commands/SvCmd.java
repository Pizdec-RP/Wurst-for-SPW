/*

 *



 */
package net.purefps.commands;

import net.minecraft.client.network.ServerInfo;
import net.purefps.command.CmdError;
import net.purefps.command.CmdException;
import net.purefps.command.CmdSyntaxError;
import net.purefps.command.Command;
import net.purefps.util.ChatUtils;
import net.purefps.util.LastServerRememberer;

public final class SvCmd extends Command
{
	public SvCmd()
	{
		super("sv", "Shows the version of the server\n"
			+ "you are currently connected to.", ".sv");
	}

	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length != 0)
			throw new CmdSyntaxError();

		ChatUtils.message("Server version: " + getVersion());
	}

	private String getVersion() throws CmdError
	{
		if(MC.isIntegratedServerRunning())
			throw new CmdError("Can't check server version in singleplayer.");

		ServerInfo lastServer = LastServerRememberer.getLastServer();
		if(lastServer == null)
			throw new IllegalStateException(
				"LastServerRememberer doesn't remember the last server!");

		return lastServer.version.getString();
	}

	@Override
	public String getPrimaryAction()
	{
		return "Get Server Version";
	}

	@Override
	public void doPrimaryAction()
	{
		WURST.getCmdProcessor().process("sv");
	}
}
