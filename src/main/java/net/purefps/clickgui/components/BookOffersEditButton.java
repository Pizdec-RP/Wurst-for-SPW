/*

 *



 */
package net.purefps.clickgui.components;

import java.util.Objects;

import net.purefps.clickgui.screens.EditBookOffersScreen;
import net.purefps.settings.BookOffersSetting;
import net.purefps.settings.Setting;

public final class BookOffersEditButton extends AbstractListEditButton
{
	private final BookOffersSetting setting;

	public BookOffersEditButton(BookOffersSetting setting)
	{
		this.setting = Objects.requireNonNull(setting);
		setWidth(getDefaultWidth());
		setHeight(getDefaultHeight());
	}

	@Override
	protected void openScreen()
	{
		MC.setScreen(new EditBookOffersScreen(MC.currentScreen, setting));
	}

	@Override
	protected String getText()
	{
		return setting.getName() + ": " + setting.getOffers().size();
	}

	@Override
	protected Setting getSetting()
	{
		return setting;
	}
}
