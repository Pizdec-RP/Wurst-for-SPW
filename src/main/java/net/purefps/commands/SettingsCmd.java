/*

 *



 */
package net.purefps.commands;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;

import net.purefps.DontBlock;
import net.purefps.command.CmdError;
import net.purefps.command.CmdException;
import net.purefps.command.CmdSyntaxError;
import net.purefps.command.Command;
import net.purefps.util.ChatUtils;
import net.purefps.util.MathUtils;
import net.purefps.util.json.JsonException;

@DontBlock
public final class SettingsCmd extends Command
{
	public SettingsCmd()
	{
		super("settings", "Allows you to make profiles of your settings.",
			".settings load-profile <file>", ".settings save-profile <file>",
			".settings list-profiles [<page>]",
			"Profiles are saved in '.minecraft/wurst/settings'.");
	}

	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length < 1)
			throw new CmdSyntaxError();

		switch(args[0].toLowerCase())
		{
			case "load-profile":
			loadProfile(args);
			break;

			case "save-profile":
			saveProfile(args);
			break;

			case "list-profiles":
			listProfiles(args);
			break;

			default:
			throw new CmdSyntaxError();
		}
	}

	private void loadProfile(String[] args) throws CmdException
	{
		if(args.length != 2)
			throw new CmdSyntaxError();

		String name = parseFileName(args[1]);

		try
		{
			WURST.loadSettingsProfile(name);
			ChatUtils.message("Settings loaded: " + name);

		}catch(NoSuchFileException e)
		{
			throw new CmdError("Profile '" + name + "' doesn't exist.");

		}catch(JsonException e)
		{
			e.printStackTrace();
			throw new CmdError(
				"Profile '" + name + "' is corrupted: " + e.getMessage());

		}catch(IOException e)
		{
			e.printStackTrace();
			throw new CmdError("Couldn't load profile: " + e.getMessage());
		}
	}

	private void saveProfile(String[] args) throws CmdException
	{
		if(args.length != 2)
			throw new CmdSyntaxError();

		String name = parseFileName(args[1]);

		try
		{
			WURST.saveSettingsProfile(name);
			ChatUtils.message("Settings saved: " + name);

		}catch(IOException | JsonException e)
		{
			e.printStackTrace();
			throw new CmdError("Couldn't save profile: " + e.getMessage());
		}
	}

	private String parseFileName(String input)
	{
		String fileName = input;
		if(!fileName.endsWith(".json"))
			fileName += ".json";

		return fileName;
	}

	private void listProfiles(String[] args) throws CmdException
	{
		if(args.length > 2)
			throw new CmdSyntaxError();

		ArrayList<Path> files = WURST.listSettingsProfiles();
		int page = parsePage(args);
		int pages = (int)Math.ceil(files.size() / 8.0);
		pages = Math.max(pages, 1);

		if(page > pages || page < 1)
			throw new CmdSyntaxError("Invalid page: " + page);

		String total = "Total: " + files.size() + " profile";
		total += files.size() != 1 ? "s" : "";
		ChatUtils.message(total);

		int start = (page - 1) * 8;
		int end = Math.min(page * 8, files.size());

		ChatUtils
			.message("Settings profile list (page " + page + "/" + pages + ")");
		for(int i = start; i < end; i++)
			ChatUtils.message(files.get(i).getFileName().toString());
	}

	private int parsePage(String[] args) throws CmdSyntaxError
	{
		if(args.length < 2)
			return 1;

		if(!MathUtils.isInteger(args[1]))
			throw new CmdSyntaxError("Not a number: " + args[1]);

		return Integer.parseInt(args[1]);
	}
}
