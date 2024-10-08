/*

 *



 */
package net.purefps.commands;

import net.purefps.command.CmdError;
import net.purefps.command.CmdException;
import net.purefps.command.CmdSyntaxError;
import net.purefps.command.Command;

public final class JumpCmd extends Command
{
	public JumpCmd()
	{
		super("jump", "Makes you jump.");
	}

	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length != 0)
			throw new CmdSyntaxError();

		if(!MC.player.isOnGround() && !WURST.getHax().jetpackHack.isEnabled())
			throw new CmdError("Can't jump in mid-air.");

		MC.player.jump();
	}

	@Override
	public String getPrimaryAction()
	{
		return "Jump";
	}

	@Override
	public void doPrimaryAction()
	{
		WURST.getCmdProcessor().process("jump");
	}
}
