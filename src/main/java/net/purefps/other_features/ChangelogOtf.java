/*

 *



 */
package net.purefps.other_features;

import net.minecraft.util.Util;
import net.purefps.DontBlock;
import net.purefps.SearchTags;
import net.purefps.PFPSClient;
import net.purefps.other_feature.OtherFeature;
import net.purefps.update.Version;

@SearchTags({"change log", "wurst update", "release notes", "what's new",
	"what is new", "new features", "recently added features"})
@DontBlock
public final class ChangelogOtf extends OtherFeature
{
	public ChangelogOtf()
	{
		super("Changelog", "Opens the changelog in your browser.");
	}
	
	@Override
	public String getPrimaryAction()
	{
		return "View Changelog";
	}
	
	@Override
	public void doPrimaryAction()
	{
		String link = new Version(PFPSClient.VERSION).getChangelogLink()
			+ "?utm_source=Wurst+Client&utm_medium=ChangelogOtf&utm_content=View+Changelog";
		Util.getOperatingSystem().open(link);
	}
}
