/*

 *



 */
package net.purefps.modules;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.math.Box;
import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.mixinterface.IKeyBinding;
import net.purefps.module.Hack;
import net.purefps.settings.CheckboxSetting;
import net.purefps.settings.SliderSetting;
import net.purefps.settings.SliderSetting.ValueDisplay;

@SearchTags({"safe walk"})
public final class SafeWalkHack extends Hack
{
	private final CheckboxSetting sneak =
		new CheckboxSetting("Sneak at edges", "Visibly sneak at edges.", false);

	private final SliderSetting edgeDistance = new SliderSetting(
		"Sneak edge distance",
		"How close SafeWalk will let you get to the edge before sneaking.\n\n"
			+ "This setting is only used when \"Sneak at edges\" is enabled.",
		0.05, 0.05, 0.25, 0.001, ValueDisplay.DECIMAL.withSuffix("m"));

	private boolean sneaking;

	public SafeWalkHack()
	{
		super("SafeWalk");
		setCategory(Category.MOVEMENT);
		addSetting(sneak);
		addSetting(edgeDistance);
	}

	@Override
	protected void onEnable()
	{
		WURST.getHax().parkourHack.setEnabled(false);
		sneaking = false;
	}

	@Override
	protected void onDisable()
	{
		if(sneaking)
			setSneaking(false);
	}

	public void onClipAtLedge(boolean clipping)
	{
		ClientPlayerEntity player = MC.player;

		if(!isEnabled() || !sneak.isChecked() || !player.isOnGround())
		{
			if(sneaking)
				setSneaking(false);

			return;
		}

		Box box = player.getBoundingBox();
		Box adjustedBox = box.stretch(0, -player.stepHeight, 0)
			.expand(-edgeDistance.getValue(), 0, -edgeDistance.getValue());

		if(MC.world.isSpaceEmpty(player, adjustedBox))
			clipping = true;

		setSneaking(clipping);
	}

	private void setSneaking(boolean sneaking)
	{
		KeyBinding sneakKey = MC.options.sneakKey;

		if(sneaking)
			sneakKey.setPressed(true);
		else
			((IKeyBinding)sneakKey).resetPressedState();

		this.sneaking = sneaking;
	}

	// See ClientPlayerEntityMixin
}
