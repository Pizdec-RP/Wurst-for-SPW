/*

 *



 */
package net.purefps.commands;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.purefps.command.CmdError;
import net.purefps.command.CmdException;
import net.purefps.command.CmdSyntaxError;
import net.purefps.command.Command;
import net.purefps.util.BlockUtils;
import net.purefps.util.MathUtils;

public final class VClipCmd extends Command
{
	public VClipCmd()
	{
		super("vclip",
			"Lets you clip through blocks vertically.\n"
				+ "The maximum distance is 10 blocks.",
			".vclip <height>", ".vclip (up|down)");
	}

	@Override
	public void call(String[] args) throws CmdException
	{
		if(args.length != 1)
			throw new CmdSyntaxError();

		if(MathUtils.isDouble(args[0]))
		{
			vclip(Double.parseDouble(args[0]));
			return;
		}

		switch(args[0].toLowerCase())
		{
			case "up":
			vclip(calculateHeight(Direction.UP));
			break;

			case "down":
			vclip(calculateHeight(Direction.DOWN));
			break;

			default:
			throw new CmdSyntaxError();
		}
	}

	private double calculateHeight(Direction direction) throws CmdError
	{
		Box box = MC.player.getBoundingBox();

		Box maxOffsetBox = box.offset(0, direction.getOffsetY() * 10, 0);
		if(!hasCollisions(box.union(maxOffsetBox)))
			throw new CmdError("There is nothing to clip through!");

		for(int i = 1; i <= 10; i++)
		{
			double height = direction.getOffsetY() * i;
			Box offsetBox = box.offset(0, height, 0);

			if(hasCollisions(offsetBox))
			{
				double subBlockOffset = getSubBlockOffset(offsetBox);
				if(subBlockOffset >= 1 || height + subBlockOffset > 10)
					continue;

				Box newOffsetBox = offsetBox.offset(0, subBlockOffset, 0);
				if(hasCollisions(newOffsetBox))
					continue;

				height += subBlockOffset;
				offsetBox = newOffsetBox;
			}

			if(!hasCollisions(box.union(offsetBox)))
				continue;

			return height;
		}

		throw new CmdError("There are no free blocks where you can fit!");
	}

	private boolean hasCollisions(Box box)
	{
		return BlockUtils.getBlockCollisions(box).findAny().isPresent();
	}

	private double getSubBlockOffset(Box offsetBox)
	{
		return BlockUtils.getBlockCollisions(offsetBox)
			.mapToDouble(box -> box.maxY).max().getAsDouble() - offsetBox.minY;
	}

	private void vclip(double height)
	{
		ClientPlayerEntity p = MC.player;
		p.setPosition(p.getX(), p.getY() + height, p.getZ());
	}
}
