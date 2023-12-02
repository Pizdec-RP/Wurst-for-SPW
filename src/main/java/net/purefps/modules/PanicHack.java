/*

 *



 */
package net.purefps.modules;

import net.purefps.Category;
import net.purefps.DontBlock;
import net.purefps.SearchTags;
import net.purefps.events.UpdateListener;
import net.purefps.module.Hack;

@SearchTags({"legit", "disable"})
@DontBlock
public final class PanicHack extends Hack implements UpdateListener
{
	public PanicHack()
	{
		super("Panic");
		setCategory(Category.OTHER);
	}
	
	@Override
	public void onEnable()
	{
		EVENTS.add(UpdateListener.class, this);
	}
	
	@Override
	public void onDisable()
	{
		EVENTS.remove(UpdateListener.class, this);
	}
	
	@Override
	public void onUpdate()
	{
		for(Hack hack : WURST.getHax().getAllHax())
			if(hack.isEnabled() && hack != this)
				hack.setEnabled(false);
			
		setEnabled(false);
	}
}
