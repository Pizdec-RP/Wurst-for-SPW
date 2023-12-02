/*

 *



 */
package net.purefps.modules;

import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.module.Hack;

@SearchTags({"no overlay", "NoWaterOverlay", "no water overlay"})
public final class NoOverlayHack extends Hack
{
	public NoOverlayHack()
	{
		super("NoOverlay");
		setCategory(Category.RENDER);
	}
	
	// See CameraMixin.onGetSubmersionType() and
	// InGameOverlayRendererMixin.onRenderUnderwaterOverlay()
}
