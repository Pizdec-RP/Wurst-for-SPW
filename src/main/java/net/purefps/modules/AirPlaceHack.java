/*

 *



 */
package net.purefps.modules;

import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.events.RightClickListener;
import net.purefps.module.Hack;
import net.purefps.settings.SliderSetting;
import net.purefps.settings.SliderSetting.ValueDisplay;
import net.purefps.util.InteractionSimulator;

@SearchTags({"air place"})
public final class AirPlaceHack extends Hack implements RightClickListener
{
	private final SliderSetting range =
		new SliderSetting("Range", 5, 1, 6, 0.05, ValueDisplay.DECIMAL);

	public AirPlaceHack()
	{
		super("AirPlace");
		setCategory(Category.BLOCKS);
		addSetting(range);
	}

	@Override
	public void onEnable()
	{
		EVENTS.add(RightClickListener.class, this);
	}

	@Override
	public void onDisable()
	{
		EVENTS.remove(RightClickListener.class, this);
	}

	@Override
	public void onRightClick(RightClickEvent event)
	{
		HitResult hitResult = MC.player.raycast(range.getValue(), 0, false);
		if((hitResult.getType() != HitResult.Type.MISS) || !(hitResult instanceof BlockHitResult blockHitResult))
			return;

		MC.itemUseCooldown = 4;
		if(MC.player.isRiding())
			return;

		InteractionSimulator.rightClickBlock(blockHitResult);
		event.cancel();
	}
}
