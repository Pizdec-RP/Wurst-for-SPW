/*

 *



 */
package net.purefps.modules;

import net.purefps.Category;
import net.purefps.SearchTags;
import net.purefps.module.Hack;

@SearchTags({"camera noclip", "camera no clip"})
public final class CameraNoClipHack extends Hack
{
	public CameraNoClipHack()
	{
		super("CameraNoClip");
		setCategory(Category.RENDER);
	}

	// See CameraMixin.onClipToSpace()
}
