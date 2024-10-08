/*

 *



 */
package net.purefps.other_features;

import java.awt.Color;
import java.util.function.BooleanSupplier;

import net.purefps.DontBlock;
import net.purefps.SearchTags;
import net.purefps.other_feature.OtherFeature;
import net.purefps.settings.ColorSetting;
import net.purefps.settings.EnumSetting;

@SearchTags({"wurst logo", "top left corner"})
@DontBlock
public final class WurstLogoOtf extends OtherFeature
{
	private final ColorSetting bgColor = new ColorSetting("Background",
		"Background color.\n"
			+ "Only visible when \u00a76RainbowUI\u00a7r is disabled.",
		Color.WHITE);

	private final ColorSetting txtColor =
		new ColorSetting("Text", "Text color.", Color.BLACK);

	private final EnumSetting<Visibility> visibility =
		new EnumSetting<>("Visibility", Visibility.values(), Visibility.ALWAYS);

	public WurstLogoOtf()
	{
		super("WurstLogo", "Shows the Wurst logo and version on the screen.");
		addSetting(bgColor);
		addSetting(txtColor);
		addSetting(visibility);
	}

	public boolean isVisible()
	{
		return visibility.getSelected().isVisible();
	}

	public float[] getBackgroundColor()
	{
		return bgColor.getColorF();
	}

	public int getTextColor()
	{
		return txtColor.getColorI();
	}

	public static enum Visibility
	{
		ALWAYS("Always", () -> true),

		ONLY_OUTDATED("Never",
			() -> false);

		private final String name;
		private final BooleanSupplier visible;

		private Visibility(String name, BooleanSupplier visible)
		{
			this.name = name;
			this.visible = visible;
		}

		public boolean isVisible()
		{
			return visible.getAsBoolean();
		}

		@Override
		public String toString()
		{
			return name;
		}
	}
}
