/*

 *



 */
package net.purefps.settings;

import java.util.LinkedHashSet;
import java.util.Set;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.purefps.PFPSClient;
import net.purefps.clickgui.Component;
import net.purefps.clickgui.components.CheckboxComponent;
import net.purefps.keybinds.PossibleKeybind;
import net.purefps.util.json.JsonUtils;

public class CheckboxSetting extends Setting implements CheckboxLock
{
	private boolean checked;
	private final boolean checkedByDefault;
	private CheckboxLock lock;

	public CheckboxSetting(String name, String description, boolean checked)
	{
		super(name, description);
		this.checked = checked;
		checkedByDefault = checked;
	}

	public CheckboxSetting(String name, boolean checked)
	{
		this(name, "", checked);
	}

	@Override
	public final boolean isChecked()
	{
		return isLocked() ? lock.isChecked() : checked;
	}

	public final boolean isCheckedByDefault()
	{
		return checkedByDefault;
	}

	public final void setChecked(boolean checked)
	{
		if(isLocked())
			return;

		setCheckedIgnoreLock(checked);
	}

	private void setCheckedIgnoreLock(boolean checked)
	{
		this.checked = checked;
		update();

		PFPSClient.INSTANCE.saveSettings();
	}

	public final boolean isLocked()
	{
		return lock != null;
	}

	public final void lock(CheckboxLock lock)
	{
		this.lock = lock;
		update();
	}

	public final void unlock()
	{
		lock = null;
		update();
	}

	@Override
	public final Component getComponent()
	{
		return new CheckboxComponent(this);
	}

	@Override
	public final void fromJson(JsonElement json)
	{
		if(!JsonUtils.isBoolean(json))
			return;

		setCheckedIgnoreLock(json.getAsBoolean());
	}

	@Override
	public final JsonElement toJson()
	{
		return new JsonPrimitive(checked);
	}

	@Override
	public JsonObject exportWikiData()
	{
		JsonObject json = new JsonObject();
		json.addProperty("name", getName());
		json.addProperty("descriptionKey", getDescriptionKey());
		json.addProperty("type", "Checkbox");
		json.addProperty("checkedByDefault", checkedByDefault);
		return json;
	}

	@Override
	public final Set<PossibleKeybind> getPossibleKeybinds(String featureName)
	{
		String fullName = featureName + " " + getName();

		String command = ".setcheckbox " + featureName.toLowerCase() + " ";
		command += getName().toLowerCase().replace(" ", "_") + " ";

		LinkedHashSet<PossibleKeybind> pkb = new LinkedHashSet<>();
		pkb.add(new PossibleKeybind(command + "toggle", "Toggle " + fullName));
		pkb.add(new PossibleKeybind(command + "on", "Check " + fullName));
		pkb.add(new PossibleKeybind(command + "off", "Uncheck " + fullName));

		return pkb;
	}
}
