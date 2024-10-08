/*

 *



 */
package net.purefps.clickgui.screens;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.purefps.clickgui.ClickGui;

public final class ClickGuiScreen extends Screen
{
	private final ClickGui gui;

	public ClickGuiScreen(ClickGui gui)
	{
		super(Text.literal(""));
		this.gui = gui;
	}

	@Override
	public boolean shouldPause()
	{
		return false;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
	{
		gui.handleMouseClick((int)mouseX, (int)mouseY, mouseButton);
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int mouseButton)
	{
		gui.handleMouseRelease(mouseX, mouseY, mouseButton);
		return super.mouseReleased(mouseX, mouseY, mouseButton);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY,
		double horizontalAmount, double verticalAmount)
	{
		gui.handleMouseScroll(mouseX, mouseY, verticalAmount);
		return super.mouseScrolled(mouseX, mouseY, horizontalAmount,
			verticalAmount);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY,
		float partialTicks)
	{
		super.render(context, mouseX, mouseY, partialTicks);
		gui.render(context, mouseX, mouseY, partialTicks);
	}
}
