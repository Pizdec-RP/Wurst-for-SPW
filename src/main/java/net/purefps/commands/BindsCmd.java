/*

 *



 */
package net.purefps.commands;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.util.InputUtil;
import net.purefps.DontBlock;
import net.purefps.command.CmdError;
import net.purefps.command.CmdException;
import net.purefps.command.CmdSyntaxError;
import net.purefps.command.Command;
import net.purefps.keybinds.Keybind;
import net.purefps.keybinds.KeybindList;
import net.purefps.util.ChatUtils;
import net.purefps.util.MathUtils;
import net.purefps.util.json.JsonException;

@DontBlock
public final class BindsCmd extends Command
{
	public BindsCmd()
	{
		super("binds", "Allows you to manage keybinds through the chat.",
			".binds add <key> <hacks>", ".binds add <key> <commands>",
			".binds remove <key>", ".binds list [<page>]",
			".binds load-profile <file>", ".binds save-profile <file>",
			".binds list-profiles [<page>]", ".binds remove-all",
			".binds reset", "Multiple hacks/commands must be separated by ';'.",
			"Profiles are saved in '.minecraft/wurst/keybinds'.");
	}

	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length < 1)
			throw new CmdSyntaxError();

		switch(args[0].toLowerCase())
		{
			case "add":
			add(args);
			break;

			case "remove":
			remove(args);
			break;

			case "list":
			list(args);
			break;

			case "load-profile":
			loadProfile(args);
			break;

			case "save-profile":
			saveProfile(args);
			break;

			case "list-profiles":
			listProfiles(args);
			break;

			case "remove-all":
			removeAll();
			break;

			case "reset":
			reset();
			break;

			default:
			throw new CmdSyntaxError();
		}
	}

	private void add(String[] args) throws CmdException
	{
		if(args.length < 3)
			throw new CmdSyntaxError();

		String displayKey = args[1];
		String key = parseKey(displayKey);
		String[] cmdArgs = Arrays.copyOfRange(args, 2, args.length);
		String commands = String.join(" ", cmdArgs);

		WURST.getKeybinds().add(key, commands);
		ChatUtils.message("Keybind set: " + displayKey + " -> " + commands);
	}

	private void remove(String[] args) throws CmdException
	{
		if(args.length != 2)
			throw new CmdSyntaxError();

		String displayKey = args[1];
		String key = parseKey(displayKey);

		String commands = WURST.getKeybinds().getCommands(key);
		if(commands == null)
			throw new CmdError("Nothing to remove.");

		WURST.getKeybinds().remove(key);
		ChatUtils.message("Keybind removed: " + displayKey + " -> " + commands);
	}

	private String parseKey(String displayKey) throws CmdSyntaxError
	{
		String key = displayKey.toLowerCase();

		String prefix = "key.keyboard.";
		if(!key.startsWith(prefix))
			key = prefix + key;

		try
		{
			InputUtil.fromTranslationKey(key);
			return key;

		}catch(IllegalArgumentException e)
		{
			throw new CmdSyntaxError("Unknown key: " + displayKey);
		}
	}

	private void list(String[] args) throws CmdException
	{
		if(args.length > 2)
			throw new CmdSyntaxError();

		List<Keybind> binds = WURST.getKeybinds().getAllKeybinds();
		int page = parsePage(args);
		int pages = (int)Math.ceil(binds.size() / 8.0);
		pages = Math.max(pages, 1);

		if(page > pages || page < 1)
			throw new CmdSyntaxError("Invalid page: " + page);

		String total = "Total: " + binds.size() + " keybind";
		total += binds.size() != 1 ? "s" : "";
		ChatUtils.message(total);

		int start = (page - 1) * 8;
		int end = Math.min(page * 8, binds.size());

		ChatUtils.message("Keybind list (page " + page + "/" + pages + ")");
		for(int i = start; i < end; i++)
			ChatUtils.message(binds.get(i).toString());
	}

	private int parsePage(String[] args) throws CmdSyntaxError
	{
		if(args.length < 2)
			return 1;

		if(!MathUtils.isInteger(args[1]))
			throw new CmdSyntaxError("Not a number: " + args[1]);

		return Integer.parseInt(args[1]);
	}

	private void removeAll()
	{
		WURST.getKeybinds().removeAll();
		ChatUtils.message("All keybinds removed.");
	}

	private void reset()
	{
		WURST.getKeybinds().setKeybinds(KeybindList.DEFAULT_KEYBINDS);
		ChatUtils.message("All keybinds reset to defaults.");
	}

	private void loadProfile(String[] args) throws CmdException
	{
		if(args.length != 2)
			throw new CmdSyntaxError();

		String name = parseFileName(args[1]);

		try
		{
			WURST.getKeybinds().loadProfile(name);
			ChatUtils.message("Keybinds loaded: " + name);

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
			WURST.getKeybinds().saveProfile(name);
			ChatUtils.message("Keybinds saved: " + name);

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

		ArrayList<Path> files = WURST.getKeybinds().listProfiles();
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
			.message("Keybind profile list (page " + page + "/" + pages + ")");
		for(int i = start; i < end; i++)
			ChatUtils.message(files.get(i).getFileName().toString());
	}
}
