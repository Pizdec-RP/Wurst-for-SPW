/*

 *



 */
package net.purefps.settings;

import java.util.Objects;
import java.util.Set;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.purefps.PFPSClient;
import net.purefps.clickgui.Component;
import net.purefps.keybinds.PossibleKeybind;
import net.purefps.util.ChatUtils;

public abstract class Setting
{
	private final String name;
	private final String description;
	
	public Setting(String name, String description)
	{
		this.name = Objects.requireNonNull(name);
		this.description = Objects.requireNonNull(description);
	}
	
	public final String getName()
	{
		return name;
	}
	
	public final String getDescription()
	{
		return PFPSClient.INSTANCE.translate(description);
	}
	
	public final String getWrappedDescription(int width)
	{
		return ChatUtils.wrapText(getDescription(), width);
	}
	
	public final String getDescriptionKey()
	{
		return description;
	}
	
	public abstract Component getComponent();
	
	public abstract void fromJson(JsonElement json);
	
	public abstract JsonElement toJson();
	
	/**
	 * Exports this setting's data to a {@link JsonObject} for use in the
	 * Wurst Wiki. Must always specify the following properties:
	 * <ul>
	 * <li>name
	 * <li>descriptionKey
	 * <li>type
	 * </ul>
	 */
	public abstract JsonObject exportWikiData();
	
	public void update()
	{
		
	}
	
	public abstract Set<PossibleKeybind> getPossibleKeybinds(
		String featureName);
}
