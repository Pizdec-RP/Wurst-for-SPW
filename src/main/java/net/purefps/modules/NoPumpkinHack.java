/*

 *



 */
package net.purefps.modules;

import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.module.Hack;

@SearchTags({"no pumpkin", "AntiPumpkin", "anti pumpkin"})
public final class NoPumpkinHack extends Hack
{
	public NoPumpkinHack()
	{
		super("NoPumpkin");
		setCategory(Category.RENDER);
	}
	
	// See IngameHudMixin.onRenderOverlay()
}
