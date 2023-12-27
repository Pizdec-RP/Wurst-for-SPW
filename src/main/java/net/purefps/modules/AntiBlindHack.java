/*

 *



 */
package net.purefps.modules;

import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.module.Hack;

@SearchTags({"AntiBlindness", "NoBlindness", "anti blindness", "no blindness",
	"AntiDarkness", "NoDarkness", "anti darkness", "no darkness",
	"AntiWardenEffect", "anti warden effect", "NoWardenEffect",
	"no warden effect"})
public final class AntiBlindHack extends Hack
{
	public AntiBlindHack()
	{
		super("AntiBlind");
		setCategory(Category.RENDER);
	}

	// See BackgroundRendererMixin, WorldRendererMixin,
	// ClientPlayerEntityMixin.hasStatusEffect()
}
