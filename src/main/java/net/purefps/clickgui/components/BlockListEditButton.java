/*

 *



 */
package net.purefps.clickgui.components;

import java.util.Objects;

import net.purefps.clickgui.screens.EditBlockListScreen;
import net.purefps.settings.BlockListSetting;
import net.purefps.settings.Setting;

public final class BlockListEditButton extends AbstractListEditButton
{
	private final BlockListSetting setting;

	public BlockListEditButton(BlockListSetting setting)
	{
		this.setting = Objects.requireNonNull(setting);
		setWidth(getDefaultWidth());
		setHeight(getDefaultHeight());
	}

	@Override
	protected void openScreen()
	{
		MC.setScreen(new EditBlockListScreen(MC.currentScreen, setting));
	}

	@Override
	protected String getText()
	{
		return setting.getName() + ": " + setting.getBlockNames().size();
	}

	@Override
	protected Setting getSetting()
	{
		return setting;
	}
}
