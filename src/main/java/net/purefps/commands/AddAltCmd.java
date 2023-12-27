/*

 *



 */
package net.purefps.commands;

import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.StringHelper;
import net.purefps.altmanager.AltManager;
import net.purefps.altmanager.CrackedAlt;
import net.purefps.command.CmdException;
import net.purefps.command.CmdSyntaxError;
import net.purefps.command.Command;
import net.purefps.util.ChatUtils;

public final class AddAltCmd extends Command
{
	public AddAltCmd()
	{
		super("addalt", "Adds a player to your alt list.", ".addalt <player>",
			"Add all players on the server: .addalt all");
	}

	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length != 1)
			throw new CmdSyntaxError();

		String name = args[0];

		switch(name)
		{
			case "all":
			addAll();
			break;

			default:
			add(name);
			break;
		}
	}

	private void add(String name)
	{
		if(name.equalsIgnoreCase("Alexander01998"))
			return;

		WURST.getAltManager().add(new CrackedAlt(name));
		ChatUtils.message("Added 1 alt.");
	}

	private void addAll()
	{
		int alts = 0;
		AltManager altManager = WURST.getAltManager();
		String playerName = MC.getSession().getUsername();

		for(PlayerListEntry entry : MC.player.networkHandler.getPlayerList())
		{
			String name = entry.getProfile().getName();
			name = StringHelper.stripTextFormat(name);

			if(altManager.contains(name) || name.equalsIgnoreCase(playerName)
				|| name.equalsIgnoreCase("Alexander01998"))
				continue;

			altManager.add(new CrackedAlt(name));
			alts++;
		}

		ChatUtils.message("Added " + alts + (alts == 1 ? " alt." : " alts."));
	}
}
