/*

 *



 */
package net.purefps.other_features;

import net.purefps.DontBlock;
import net.purefps.SearchTags;
import net.purefps.other_feature.OtherFeature;
import net.purefps.settings.CheckboxSetting;

@DontBlock
@SearchTags({"privacy", "data", "tracking", "snooper", "spyware"})
public final class NoTelemetryOtf extends OtherFeature
{
	private final CheckboxSetting disableTelemetry =
		new CheckboxSetting("Disable telemetry", true);

	public NoTelemetryOtf()
	{
		super("NoTelemetry",
			"Disables the \"required\" telemetry that Mojang introduced in 22w46a. Turns out it's not so required after all.");
		addSetting(disableTelemetry);
	}

	@Override
	public boolean isEnabled()
	{
		return disableTelemetry.isChecked();
	}

	@Override
	public String getPrimaryAction()
	{
		return isEnabled() ? "Re-enable Telemetry" : "Disable Telemetry";
	}

	@Override
	public void doPrimaryAction()
	{
		disableTelemetry.setChecked(!disableTelemetry.isChecked());
	}

	// See TelemetrySenderMixin
}
