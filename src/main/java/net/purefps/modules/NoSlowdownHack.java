/*

 *



 */
package net.purefps.modules;

import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.module.Hack;

@SearchTags({"no slowdown", "no slow down"})
public final class NoSlowdownHack extends Hack
{
	public NoSlowdownHack()
	{
		super("NoSlowdown");
		setCategory(Category.MOVEMENT);
	}
	
	// See BlockMixin.onGetVelocityMultiplier() and
	// ClientPlayerEntityMixin.wurstIsUsingItem()
}
