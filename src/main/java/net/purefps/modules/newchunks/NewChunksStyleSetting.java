/*

 *



 */
package net.purefps.modules.newchunks;

import net.purefps.settings.EnumSetting;

public final class NewChunksStyleSetting
	extends EnumSetting<NewChunksStyleSetting.Style>
{
	public NewChunksStyleSetting()
	{
		super("Style", Style.values(), Style.OUTLINE);
	}

	public static enum Style
	{
		OUTLINE("Outline", new NewChunksOutlineRenderer()),
		SQUARE("Square", new NewChunksSquareRenderer());

		private final String name;
		private final NewChunksChunkRenderer chunkRenderer;

		private Style(String name, NewChunksChunkRenderer chunkRenderer)
		{
			this.name = name;
			this.chunkRenderer = chunkRenderer;
		}

		@Override
		public String toString()
		{
			return name;
		}

		public NewChunksChunkRenderer getChunkRenderer()
		{
			return chunkRenderer;
		}
	}
}
