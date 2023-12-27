/*

 *



 */
package net.purefps.modules;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.module.Hack;
import net.purefps.settings.CheckboxSetting;

@SearchTags({"no background", "NoGuiBackground", "no gui background",
	"NoGradient", "no gradient"})
public final class NoBackgroundHack extends Hack
{
	public final CheckboxSetting allGuis = new CheckboxSetting("All GUIs",
		"Removes the background for all GUIs, not just inventories.", false);

	public NoBackgroundHack()
	{
		super("NoBackground");
		setCategory(Category.RENDER);
		addSetting(allGuis);
	}

	public boolean shouldCancelBackground(Screen screen)
	{
		if(!isEnabled() || (MC.world == null) || (!allGuis.isChecked() && !(screen instanceof HandledScreen)))
			return false;

		return true;
	}

	// See ScreenMixin.onRenderBackground()
}
