/*

 *



 */
package net.purefps.modules;

import java.util.Random;

import net.minecraft.client.render.entity.PlayerModelPart;
import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.events.UpdateListener;
import net.purefps.module.Hack;

@SearchTags({"SpookySkin", "spooky skin", "SkinBlinker", "skin blinker"})
public final class SkinDerpHack extends Hack implements UpdateListener
{
	private final Random random = new Random();

	public SkinDerpHack()
	{
		super("SkinDerp");
		setCategory(Category.FUN);
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

		for(PlayerModelPart part : PlayerModelPart.values())
			MC.options.togglePlayerModelPart(part, true);
	}

	@Override
	public void onUpdate()
	{
		if(random.nextInt(4) != 0)
			return;

		for(PlayerModelPart part : PlayerModelPart.values())
			MC.options.togglePlayerModelPart(part,
				!MC.options.isPlayerModelPartEnabled(part));
	}
}
