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
import net.purefps.settings.Setting;
import net.purefps.settings.SliderSetting;
import net.purefps.util.CmdUtils;
import net.purefps.util.MathUtils;

@DontBlock
public final class SetSliderCmd extends Command
{
	public SetSliderCmd()
	{
		super("setslider",
			"Changes a slider setting of a feature. Allows you to\n"
				+ "move sliders through keybinds.",
			".setslider <feature> <setting> <value>",
			".setslider <feature> <setting> (more|less)");
	}

	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length != 3)
			throw new CmdSyntaxError();

		Feature feature = CmdUtils.findFeature(args[0]);
		Setting setting = CmdUtils.findSetting(feature, args[1]);
		SliderSetting slider = getAsSlider(feature, setting);
		setValue(args[2], slider);
	}

	private SliderSetting getAsSlider(Feature feature, Setting setting)
		throws CmdError
	{
		if(!(setting instanceof SliderSetting))
			throw new CmdError(feature.getName() + " " + setting.getName()
				+ " is not a slider setting.");

		return (SliderSetting)setting;
	}

	private void setValue(String value, SliderSetting slider)
		throws CmdSyntaxError
	{
		switch(value.toLowerCase())
		{
			case "more":
			slider.increaseValue();
			break;

			case "less":
			slider.decreaseValue();
			break;

			default:
			if(!MathUtils.isDouble(value))
				throw new CmdSyntaxError("Value must be a number.");
			slider.setValue(Double.parseDouble(value));
			break;
		}
	}
}
