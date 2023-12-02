/*

 *



 */
package net.purefps.modules;

import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.module.Hack;

@SearchTags({"no levitation", "levitation", "levitate"})
public final class NoLevitationHack extends Hack
{
	public NoLevitationHack()
	{
		super("NoLevitation");
		setCategory(Category.MOVEMENT);
	}
	
	// See ClientPlayerEntityMixin.hasStatusEffect()
}
