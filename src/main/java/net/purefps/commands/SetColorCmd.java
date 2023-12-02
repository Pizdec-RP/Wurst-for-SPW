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
import net.purefps.settings.ColorSetting;
import net.purefps.settings.Setting;
import net.purefps.util.CmdUtils;
import net.purefps.util.ColorUtils;
import net.purefps.util.json.JsonException;

@DontBlock
public final class SetColorCmd extends Command
{
	public SetColorCmd()
	{
		super("setcolor",
			"Changes a color setting of a feature. Allows you\n"
				+ "to set RGB values through keybinds.",
			".setcolor <feature> <setting> <RGB>",
			"Example: .setcolor ClickGUI AC #FF0000");
	}
	
	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length != 3)
			throw new CmdSyntaxError();
		
		Feature feature = CmdUtils.findFeature(args[0]);
		Setting setting = CmdUtils.findSetting(feature, args[1]);
		ColorSetting colorSetting = getAsColor(feature, setting);
		setColor(colorSetting, args[2]);
	}
	
	private ColorSetting getAsColor(Feature feature, Setting setting)
		throws CmdError
	{
		if(!(setting instanceof ColorSetting))
			throw new CmdError(feature.getName() + " " + setting.getName()
				+ " is not a color setting.");
		
		return (ColorSetting)setting;
	}
	
	private void setColor(ColorSetting setting, String value)
		throws CmdSyntaxError
	{
		try
		{
			setting.setColor(ColorUtils.parseHex(value));
			
		}catch(JsonException e)
		{
			throw new CmdSyntaxError("Invalid color: " + value);
		}
	}
}
