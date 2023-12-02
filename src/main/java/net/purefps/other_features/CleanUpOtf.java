/*

 *



 */
package net.purefps.other_features;

import net.purefps.DontBlock;
import net.purefps.SearchTags;
import net.purefps.other_feature.OtherFeature;

@SearchTags({"Clean Up"})
@DontBlock
public final class CleanUpOtf extends OtherFeature
{
	public CleanUpOtf()
	{
		super("CleanUp", "Cleans up your server list.\n"
			+ "To use it, press the 'Clean Up' button on the server selection screen.");
	}
}
