/*

 *



 */
package net.purefps.clickgui;

import net.minecraft.client.gui.DrawContext;

public abstract class Component
{
	private int x;
	private int y;
	private int width;
	private int height;

	private Window parent;

	public void handleMouseClick(double mouseX, double mouseY, int mouseButton)
	{

	}

	public abstract void render(DrawContext context, int mouseX, int mouseY,
		float partialTicks);

	public abstract int getDefaultWidth();

	public abstract int getDefaultHeight();

	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		if(this.x != x)
			invalidateParent();

		this.x = x;
	}

	public int getY()
	{
		return y;
	}

	public void setY(int y)
	{
		if(this.y != y)
			invalidateParent();

		this.y = y;
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		if(this.width != width)
			invalidateParent();

		this.width = width;
	}

	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		if(this.height != height)
			invalidateParent();

		this.height = height;
	}

	public Window getParent()
	{
		return parent;
	}

	public void setParent(Window parent)
	{
		this.parent = parent;
	}

	private void invalidateParent()
	{
		if(parent != null)
			parent.invalidate();
	}
}
