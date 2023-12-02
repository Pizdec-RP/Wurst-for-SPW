/*

 *



 */
package net.purefps.other_features;

import net.purefps.DontBlock;
import net.purefps.SearchTags;
import net.purefps.other_feature.OtherFeature;
import net.purefps.settings.CheckboxSetting;

@SearchTags({"turn off", "hide wurst logo", "ghost mode", "stealth mode",
	"vanilla Minecraft"})
@DontBlock
public final class DisableOtf extends OtherFeature
{
	private final CheckboxSetting hideEnableButton = new CheckboxSetting(
		"Hide enable button",
		"Removes the \"Enable Wurst\" button as soon as you close the Statistics screen."
			+ " You will have to restart the game to re-enable Wurst.",
		false);
	
	public DisableOtf()
	{
		super("Disable Wurst",
			"To disable Wurst, go to the Statistics screen and press the \"Disable Wurst\" button.\n"
				+ "It will turn into an \"Enable Wurst\" button once pressed.");
		addSetting(hideEnableButton);
	}
	
	public boolean shouldHideEnableButton()
	{
		return !WURST.isEnabled() && hideEnableButton.isChecked();
	}
}
