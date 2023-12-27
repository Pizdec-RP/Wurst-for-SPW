/*

 *



 */
package net.purefps.modules;

import net.purefps.Category;
import net.purefps.module.Hack;
import net.purefps.settings.SliderSetting;
import net.purefps.settings.SliderSetting.ValueDisplay;

public final class TimerHack extends Hack
{
	private final SliderSetting speed =
		new SliderSetting("Speed", 2, 0.1, 20, 0.01, ValueDisplay.DECIMAL);

	public TimerHack()
	{
		super("Timer");
		setCategory(Category.OTHER);
		addSetting(speed);
	}

	@Override
	public String getRenderName()
	{
		return getName() + " [" + speed.getValueString() + "]";
	}

	public float getTimerSpeed()
	{
		return isEnabled() ? speed.getValueF() : 1;
	}
}
