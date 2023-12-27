/*

 *



 */
package net.purefps.other_features;

import net.purefps.DontBlock;
import net.purefps.SearchTags;
import net.purefps.other_feature.OtherFeature;
import net.purefps.settings.CheckboxSetting;

@SearchTags({"languages", "localizations", "localisations",
	"internationalization", "internationalisation", "i18n", "sprachen",
	"übersetzungen", "force english"})
@DontBlock
public final class TranslationsOtf extends OtherFeature
{
	private final CheckboxSetting forceEnglish = new CheckboxSetting(
		"Force English",
		"Displays the Wurst Client in English, even if Minecraft is set to a different language.",
		true);

	public TranslationsOtf()
	{
		super("Translations", "Allows text in Wurst to be displayed"
			+ " in other languages than English. It will use the same language"
			+ " that Minecraft is set to.\n\n"
			+ "This is an experimental feature!");
		addSetting(forceEnglish);
	}

	public CheckboxSetting getForceEnglish()
	{
		return forceEnglish;
	}
}
