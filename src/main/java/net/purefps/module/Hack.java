/*

 *



 */
package net.purefps.module;

import java.util.Objects;

import net.purefps.Category;
import net.purefps.Feature;
import net.purefps.modules.ClickGuiHack;
import net.purefps.modules.NavigatorHack;
import net.purefps.modules.TooManyHaxHack;

public abstract class Hack extends Feature
{
	private final String name;
	private final String description;
	private Category category;

	private boolean enabled;
	private final boolean stateSaved =
		!getClass().isAnnotationPresent(DontSaveState.class);

	public Hack(String name)
	{
		this.name = Objects.requireNonNull(name);
		description = "description.purefps.hack." + name.toLowerCase();
		addPossibleKeybind(name, "Toggle " + name);
	}

	@Override
	public final String getName()
	{
		return name;
	}

	public String getRenderName()
	{
		return name;
	}

	@Override
	public final String getDescription()
	{
		return WURST.translate(description);
	}

	public final String getDescriptionKey()
	{
		return description;
	}

	@Override
	public final Category getCategory()
	{
		return category;
	}

	protected final void setCategory(Category category)
	{
		this.category = category;
	}

	@Override
	public final boolean isEnabled()
	{
		return enabled;
	}

	public final void setEnabled(boolean enabled)
	{
		if(this.enabled == enabled)
			return;

		TooManyHaxHack tooManyHax = WURST.getHax().tooManyHaxHack;
		if(enabled && tooManyHax.isEnabled() && tooManyHax.isBlocked(this))
			return;

		this.enabled = enabled;

		if(!(this instanceof NavigatorHack || this instanceof ClickGuiHack))
			WURST.getHud().getHackList().updateState(this);

		if(enabled)
			onEnable();
		else
			onDisable();

		if(stateSaved)
			WURST.getHax().saveEnabledHax();
	}

	@Override
	public final String getPrimaryAction()
	{
		return enabled ? "Disable" : "Enable";
	}

	@Override
	public final void doPrimaryAction()
	{
		setEnabled(!enabled);
	}

	public final boolean isStateSaved()
	{
		return stateSaved;
	}

	protected void onEnable()
	{

	}

	protected void onDisable()
	{

	}
}
