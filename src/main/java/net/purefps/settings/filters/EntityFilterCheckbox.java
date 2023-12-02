/*

 *



 */
package net.purefps.settings.filters;

import net.purefps.settings.CheckboxSetting;
import net.purefps.settings.Setting;
import net.purefps.settings.filterlists.EntityFilterList.EntityFilter;

public abstract class EntityFilterCheckbox extends CheckboxSetting
	implements EntityFilter
{
	public EntityFilterCheckbox(String name, String description,
		boolean checked)
	{
		super(name, description, checked);
	}
	
	@Override
	public final boolean isFilterEnabled()
	{
		return isChecked();
	}
	
	@Override
	public final Setting getSetting()
	{
		return this;
	}
}
