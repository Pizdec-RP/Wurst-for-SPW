/*

 *



 */
package net.purefps.settings;

public final class EspBoxSizeSetting
	extends EnumSetting<EspBoxSizeSetting.BoxSize>
{
	public EspBoxSizeSetting(String description)
	{
		super("Box size", description, BoxSize.values(), BoxSize.FANCY);
	}

	public EspBoxSizeSetting(String name, String description, BoxSize selected)
	{
		super(name, description, BoxSize.values(), selected);
	}

	public float getExtraSize()
	{
		return getSelected().extraSize;
	}

	public enum BoxSize
	{
		ACCURATE("Accurate", 0),
		FANCY("Fancy", 0.1F);

		private final String name;
		private final float extraSize;

		private BoxSize(String name, float extraSize)
		{
			this.name = name;
			this.extraSize = extraSize;
		}

		public float getExtraSize()
		{
			return extraSize;
		}

		@Override
		public String toString()
		{
			return name;
		}
	}
}
