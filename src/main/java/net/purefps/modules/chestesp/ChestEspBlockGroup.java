/*

 *



 */
package net.purefps.modules.chestesp;

import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.purefps.settings.CheckboxSetting;
import net.purefps.settings.ColorSetting;
import net.purefps.util.BlockUtils;

public final class ChestEspBlockGroup extends ChestEspGroup
{
	public ChestEspBlockGroup(ColorSetting color, CheckboxSetting enabled)
	{
		super(color, enabled);
	}

	public void add(BlockEntity be)
	{
		Box box = getBox(be);
		if(box == null)
			return;

		boxes.add(box);
	}

	private Box getBox(BlockEntity be)
	{
		BlockPos pos = be.getPos();

		if(!BlockUtils.canBeClicked(pos))
			return null;

		if(be instanceof ChestBlockEntity)
			return getChestBox((ChestBlockEntity)be);

		return BlockUtils.getBoundingBox(pos);
	}

	private Box getChestBox(ChestBlockEntity chestBE)
	{
		BlockState state = chestBE.getCachedState();
		if(!state.contains(ChestBlock.CHEST_TYPE))
			return null;

		ChestType chestType = state.get(ChestBlock.CHEST_TYPE);

		// ignore other block in double chest
		if(chestType == ChestType.LEFT)
			return null;

		BlockPos pos = chestBE.getPos();
		Box box = BlockUtils.getBoundingBox(pos);

		// larger box for double chest
		if(chestType != ChestType.SINGLE)
		{
			BlockPos pos2 = pos.offset(ChestBlock.getFacing(state));

			if(BlockUtils.canBeClicked(pos2))
			{
				Box box2 = BlockUtils.getBoundingBox(pos2);
				box = box.union(box2);
			}
		}

		return box;
	}

}
