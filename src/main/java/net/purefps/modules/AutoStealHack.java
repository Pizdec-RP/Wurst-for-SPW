/*

 *



 */
package net.purefps.modules;

import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.module.Hack;
import net.purefps.settings.CheckboxSetting;
import net.purefps.settings.SliderSetting;
import net.purefps.settings.SliderSetting.ValueDisplay;

@SearchTags({"auto steal", "ChestStealer", "chest stealer",
	"steal store buttons", "Steal/Store buttons"})
public final class AutoStealHack extends Hack
{
	private final SliderSetting delay = new SliderSetting("Delay",
		"Delay between moving stacks of items.\n"
			+ "Should be at least 70ms for NoCheat+ servers.",
		100, 0, 500, 10, ValueDisplay.INTEGER.withSuffix("ms"));
	
	private final CheckboxSetting buttons =
		new CheckboxSetting("Steal/Store buttons", true);
	
	public AutoStealHack()
	{
		super("AutoSteal");
		setCategory(Category.ITEMS);
		addSetting(buttons);
		addSetting(delay);
	}
	
	public boolean areButtonsVisible()
	{
		return buttons.isChecked();
	}
	
	public long getDelay()
	{
		return delay.getValueI();
	}
	
	// See ContainerScreen54Mixin and ShulkerBoxScreenMixin
}
