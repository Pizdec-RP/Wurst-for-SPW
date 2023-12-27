/*

 *



 */
package net.purefps.treerender;

import net.minecraft.block.BlockState;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.purefps.util.BlockUtils;

public enum TreeBotUtils
{
	;

	public static boolean isLog(BlockPos pos)
	{
		return BlockUtils.getState(pos).isIn(BlockTags.LOGS);
	}

	public static boolean isLeaves(BlockPos pos)
	{
		BlockState state = BlockUtils.getState(pos);
		return state.isIn(BlockTags.LEAVES)
			|| state.isIn(BlockTags.WART_BLOCKS);
	}
}
