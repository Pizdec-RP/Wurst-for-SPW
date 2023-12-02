/*

 *



 */
package net.purefps.modules;

import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.module.Hack;

@SearchTags({"no hurtcam", "no hurt cam"})
public final class NoHurtcamHack extends Hack
{
	public NoHurtcamHack()
	{
		super("NoHurtcam");
		setCategory(Category.RENDER);
	}
	
	// See GameRendererMixin.onBobViewWhenHurt()
}
