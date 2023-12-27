/*

 *



 */
package net.purefps.other_features;

import net.purefps.DontBlock;
import net.purefps.SearchTags;
import net.purefps.options.KeybindManagerScreen;
import net.purefps.other_feature.OtherFeature;

@SearchTags({"KeybindManager", "keybind manager", "KeybindsManager",
	"keybinds manager"})
@DontBlock
public final class KeybindManagerOtf extends OtherFeature
{
	public KeybindManagerOtf()
	{
		super("Keybinds",
			"This is just a shortcut to let you open the Keybind Manager from within the GUI. Normally you would go to Wurst Options > Keybinds.");
	}

	@Override
	public String getPrimaryAction()
	{
		return "Open Keybind Manager";
	}

	@Override
	public void doPrimaryAction()
	{
		MC.setScreen(new KeybindManagerScreen(MC.currentScreen));
	}
}
