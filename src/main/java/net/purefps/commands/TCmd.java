/*

 *



 */
package net.purefps.commands;

import net.purefps.DontBlock;
import net.purefps.command.CmdError;
import net.purefps.command.CmdException;
import net.purefps.command.CmdSyntaxError;
import net.purefps.command.Command;
import net.purefps.module.Hack;
import net.purefps.modules.TooManyHaxHack;
import net.purefps.util.ChatUtils;

@DontBlock
public final class TCmd extends Command
{
	public TCmd()
	{
		super("t", "Toggles a hack.", ".t <hack> [on|off]", "Examples:",
			"Toggle Nuker: .t Nuker", "Disable Nuker: .t Nuker off");
	}
	
	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length < 1 || args.length > 2)
			throw new CmdSyntaxError();
		
		Hack hack = WURST.getHax().getHackByName(args[0]);
		if(hack == null)
			throw new CmdError("Unknown hack: " + args[0]);
		
		if(args.length == 1)
			setEnabled(hack, !hack.isEnabled());
		else
			switch(args[1].toLowerCase())
			{
				case "on":
				setEnabled(hack, true);
				break;
				
				case "off":
				setEnabled(hack, false);
				break;
				
				default:
				throw new CmdSyntaxError();
			}
	}
	
	private void setEnabled(Hack hack, boolean enabled)
	{
		TooManyHaxHack tooManyHax = WURST.getHax().tooManyHaxHack;
		if(!hack.isEnabled() && tooManyHax.isEnabled()
			&& tooManyHax.isBlocked(hack))
		{
			ChatUtils.error(hack.getName() + " is blocked by TooManyHax.");
			return;
		}
		
		hack.setEnabled(enabled);
	}
}
