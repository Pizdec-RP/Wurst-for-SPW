/*

 *



 */
package net.purefps.modules;

import net.purefps.Category;
import net.purefps.DontBlock;
import net.purefps.SearchTags;
import net.purefps.module.Hack;
import net.purefps.settings.SliderSetting;
import net.purefps.settings.SliderSetting.ValueDisplay;

@SearchTags({"auto reconnect", "AutoRejoin", "auto rejoin"})
@DontBlock
public final class AutoReconnectHack extends Hack
{
	private final SliderSetting waitTime =
		new SliderSetting("Wait time", "Time before reconnecting in seconds.",
			5, 0, 60, 0.5, ValueDisplay.DECIMAL.withSuffix("s"));

	public AutoReconnectHack()
	{
		super("AutoReconnect");
		setCategory(Category.OTHER);
		addSetting(waitTime);
	}

	public int getWaitTicks()
	{
		return (int)(waitTime.getValue() * 20);
	}

	// See DisconnectedScreenMixin
}
