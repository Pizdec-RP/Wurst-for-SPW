/*

 *



 */
package net.purefps.modules;

import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.module.Hack;

@SearchTags({"portal gui"})
public final class PortalGuiHack extends Hack
{
	public PortalGuiHack()
	{
		super("PortalGUI");
		setCategory(Category.OTHER);
	}
	
	// See ClientPlayerEntityMixin.beforeUpdateNausea()
}
