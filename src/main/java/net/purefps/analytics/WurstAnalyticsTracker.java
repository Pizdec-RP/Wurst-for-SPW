/*

 *



 */
package net.purefps.analytics;

import net.purefps.analytics.dmurph.JGoogleAnalyticsTracker;

public final class WurstAnalyticsTracker extends JGoogleAnalyticsTracker
{
	public WurstAnalyticsTracker(String trackingID)
	{
		super(new WurstAnalyticsConfigData(trackingID),
			GoogleAnalyticsVersion.V_4_7_2);
	}
}
