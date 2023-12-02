/*

 *



 */
package net.purefps.modules;

import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.module.Hack;

@SearchTags({"barrier esp"})
public class BarrierEspHack extends Hack
{
	public BarrierEspHack()
	{
		super("BarrierESP");
		setCategory(Category.RENDER);
	}
	
	// See ClientWorldMixin
}
