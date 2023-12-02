/*

 *



 */
package net.purefps.analytics;

import net.purefps.analytics.dmurph.AnalyticsConfigData;
import net.purefps.analytics.dmurph.VisitorData;

public final class WurstAnalyticsConfigData extends AnalyticsConfigData
{
	public WurstAnalyticsConfigData(String argTrackingCode)
	{
		super(argTrackingCode, VisitorData.newVisitor());
	}
}
