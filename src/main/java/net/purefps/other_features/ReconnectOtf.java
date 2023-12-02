/*

 *



 */
package net.purefps.other_features;

import net.purefps.DontBlock;
import net.purefps.other_feature.OtherFeature;

@DontBlock
public final class ReconnectOtf extends OtherFeature
{
	public ReconnectOtf()
	{
		super("Reconnect",
			"Whenever you get kicked from a server, Wurst gives you a \"Reconnect\" button that lets you instantly join again.");
	}
}
