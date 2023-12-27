/*

 *



 */
package net.purefps.util;

import java.util.stream.Stream;

import net.purefps.Feature;
import net.purefps.PFPSClient;
import net.purefps.command.CmdError;
import net.purefps.settings.Setting;

public enum CmdUtils
{
	;

	public static Feature findFeature(String name) throws CmdError
	{
		Stream<Feature> stream =
			PFPSClient.INSTANCE.getNavigator().getList().stream();
		stream = stream.filter(f -> name.equalsIgnoreCase(f.getName()));
		Feature feature = stream.findFirst().orElse(null);

		if(feature == null)
			throw new CmdError(
				"A feature named \"" + name + "\" could not be found.");

		return feature;
	}

	public static Setting findSetting(Feature feature, String name)
		throws CmdError
	{
		name = name.replace("_", " ").toLowerCase();
		Setting setting = feature.getSettings().get(name);

		if(setting == null)
			throw new CmdError("A setting named \"" + name
				+ "\" could not be found in " + feature.getName() + ".");

		return setting;
	}
}
