/*

 *



 */
package net.purefps.commands;

import net.purefps.DontBlock;
import net.purefps.Feature;
import net.purefps.command.CmdError;
import net.purefps.command.CmdException;
import net.purefps.command.CmdSyntaxError;
import net.purefps.command.Command;
import net.purefps.settings.EnumSetting;
import net.purefps.settings.Setting;
import net.purefps.util.CmdUtils;

@DontBlock
public final class SetModeCmd extends Command
{
	public SetModeCmd()
	{
		super("setmode",
			"Changes a mode setting of a feature. Allows you to\n"
				+ "switch modes through keybinds.",
			".setmode <feature> <setting> <mode>",
			".setmode <feature> <setting> (prev|next)");
	}

	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length != 3)
			throw new CmdSyntaxError();

		Feature feature = CmdUtils.findFeature(args[0]);
		Setting setting = CmdUtils.findSetting(feature, args[1]);
		EnumSetting<?> enumSetting = getAsEnumSetting(feature, setting);
		setMode(feature, enumSetting, args[2]);
	}

	private EnumSetting<?> getAsEnumSetting(Feature feature, Setting setting)
		throws CmdError
	{
		if(!(setting instanceof EnumSetting<?>))
			throw new CmdError(feature.getName() + " " + setting.getName()
				+ " is not a mode setting.");

		return (EnumSetting<?>)setting;
	}

	private void setMode(Feature feature, EnumSetting<?> setting, String mode)
		throws CmdError
	{
		mode = mode.replace("_", " ").toLowerCase();

		switch(mode)
		{
			case "prev":
			setting.selectPrev();
			break;

			case "next":
			setting.selectNext();
			break;

			default:
			boolean successful = setting.setSelected(mode);
			if(!successful)
				throw new CmdError(
					"A mode named '" + mode + "' in " + feature.getName() + " "
						+ setting.getName() + " could not be found.");
			break;
		}
	}
}
