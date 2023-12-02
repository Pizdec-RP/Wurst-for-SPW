/*

 *



 */
package net.purefps.modules;

import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.events.UpdateListener;
import net.purefps.module.Hack;

@SearchTags({"jet pack", "AirJump", "air jump"})
public final class JetpackHack extends Hack implements UpdateListener
{
	public JetpackHack()
	{
		super("Jetpack");
		
		setCategory(Category.MOVEMENT);
	}
	
	@Override
	public void onEnable()
	{
		WURST.getHax().creativeFlightHack.setEnabled(false);
		WURST.getHax().flightHack.setEnabled(false);
		
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
		if(MC.options.jumpKey.isPressed())
			MC.player.jump();
	}
}
