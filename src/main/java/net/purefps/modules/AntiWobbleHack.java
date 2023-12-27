/*

 *



 */
package net.purefps.modules;

import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.module.Hack;

@SearchTags({"anti wobble", "NoWobble", "no wobble", "AntiNausea",
	"anti nausea", "NoNausea", "no nausea"})
public final class AntiWobbleHack extends Hack
{
	public AntiWobbleHack()
	{
		super("AntiWobble");
		setCategory(Category.RENDER);
	}

	// See GameRendererMixin.wurstNauseaLerp()
}
