/*

 *



 */
package net.purefps.commands;

import net.purefps.SearchTags;
import net.purefps.command.CmdException;
import net.purefps.command.CmdSyntaxError;
import net.purefps.command.Command;

@SearchTags({".legit", "dots in chat", "command bypass", "prefix"})
public final class SayCmd extends Command
{
	public SayCmd()
	{
		super("say",
			"Sends the given chat message, even if it starts with a\n" + "dot.",
			".say <message>");
	}
	
	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length < 1)
			throw new CmdSyntaxError();
		
		String message = String.join(" ", args);
		if(message.startsWith("/"))
			MC.getNetworkHandler().sendChatCommand(message.substring(1));
		else
			MC.getNetworkHandler().sendChatMessage(message);
	}
}
