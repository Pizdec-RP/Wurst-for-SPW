/*

 *



 */
package net.purefps.modules;

import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.events.UpdateListener;
import net.purefps.mixinterface.IKeyBinding;
import net.purefps.module.Hack;

@SearchTags({"auto walk"})
public final class AutoWalkHack extends Hack implements UpdateListener
{
	public AutoWalkHack()
	{
		super("AutoWalk");
		setCategory(Category.MOVEMENT);
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
		((IKeyBinding)MC.options.forwardKey).resetPressedState();
	}

	@Override
	public void onUpdate()
	{
		MC.options.forwardKey.setPressed(true);
	}
}
