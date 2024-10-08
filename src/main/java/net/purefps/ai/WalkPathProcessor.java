/*

 *



 */
package net.purefps.ai;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.VineBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.purefps.PFPSClient;
import net.purefps.util.BlockUtils;
import net.purefps.util.RotationUtils;

public class WalkPathProcessor extends PathProcessor
{
	private boolean isshort = false;
	public WalkPathProcessor(ArrayList<PathPos> path)
	{
		super(path);
		if (path.size() <= 10) isshort = true;
	}

	@Override
	public void process()
	{
		// get positions
		boolean nolimit = index+1 <= path.size()-1;
		PathPos superNextPos = null;
		BlockPos pos;
		/*if(PFPSClient.MC.player.isOnGround())
			pos = BlockPos.ofFloored(PFPSClient.MC.player.getX(),
				PFPSClient.MC.player.getY() + 0.5,
				PFPSClient.MC.player.getZ());
		else*/
		pos = BlockPos.ofFloored(PFPSClient.MC.player.getPos());
		PathPos nextPos = path.get(index);

		if (nolimit) {
			superNextPos = path.get(index+1);
			if (sqrt(MC.player.getPos(), nextPos) > sqrt(MC.player.getPos(), superNextPos)) {
				index++;
				nextPos = path.get(index);
			}
		}

		int posIndex = path.indexOf(pos);

		if(posIndex == -1)
			ticksOffPath++;
		else
			ticksOffPath = 0;

		// update index
		if(pos.getX() == nextPos.getX() && pos.getZ() == nextPos.getZ() && (pos.getY() >= nextPos.getY() && pos.getY() <= nextPos.getY()+1.8f)) {
			index++;

			// disable when done
			if(index >= path.size())
				done = true;
			return;
		}
		if(posIndex > index) {
			index = posIndex + 1;

			// disable when done
			if(index >= path.size())
				done = true;
			return;
		}

		lockControls();
		PFPSClient.MC.player.getAbilities().flying = false;

		// face next position
		facePosition(nextPos);
		if(MathHelper.wrapDegrees(Math.abs(RotationUtils
			.getHorizontalAngleToLookVec(Vec3d.ofCenter(nextPos)))) > 90)
			return;

		if(WURST.getHax().jesusHack.isEnabled())
		{
			// wait for Jesus to swim up
			if(PFPSClient.MC.player.getY() < nextPos.getY()
				&& (PFPSClient.MC.player.isTouchingWater()
					|| PFPSClient.MC.player.isInLava()))
				return;

			// manually swim down if using Jesus
			if(PFPSClient.MC.player.getY() - nextPos.getY() > 0.5
				&& (PFPSClient.MC.player.isTouchingWater()
					|| PFPSClient.MC.player.isInLava()
					|| WURST.getHax().jesusHack.isOverLiquid()))
				MC.options.sneakKey.setPressed(true);
		}

		// horizontal movement
		if(pos.getX() != nextPos.getX() || pos.getZ() != nextPos.getZ())
		{
			MC.options.forwardKey.setPressed(true);
			if (nolimit && !isshort) {
				int x1 = pos.getX()-nextPos.getX(), x2 = nextPos.getX() - superNextPos.getX();
				int z1 = pos.getZ()-nextPos.getZ(), z2 = nextPos.getZ() - superNextPos.getZ();
				int y1 = pos.getY()-nextPos.getY(), y2 = nextPos.getY() - superNextPos.getY();
				if (x1 == x2 && z1 == z2) {
					MC.options.sprintKey.setPressed(true);
					if (y1 == 0 && y2 == 0) {
						MC.options.jumpKey.setPressed(true);
					}
				}
			}

			if(index > 0 && path.get(index - 1).isJumping()
				|| pos.getY() < nextPos.getY())
				MC.options.jumpKey.setPressed(true);

			// vertical movement
		}else if(pos.getY() != nextPos.getY())
			// go up
			if(pos.getY() < nextPos.getY())
			{
				// climb up
				// TODO: Spider
				Block block = BlockUtils.getBlock(pos);
				if(block instanceof LadderBlock || block instanceof VineBlock)
				{
					WURST.getRotationFaker().faceVectorClientIgnorePitch(
						BlockUtils.getBoundingBox(pos).getCenter());

					MC.options.forwardKey.setPressed(true);

				}else
				{
					// directional jump
					if(index < path.size() - 1
						&& !nextPos.up().equals(path.get(index + 1)))
						index++;

					// jump up
					MC.options.jumpKey.setPressed(true);
				}

				// go down
			}else
			{
				// skip mid-air nodes and go straight to the bottom
				while(index < path.size() - 1
					&& path.get(index).down().equals(path.get(index + 1)))
					index++;

				// walk off the edge
				if(PFPSClient.MC.player.isOnGround())
					MC.options.forwardKey.setPressed(true);
			}
	}

	@Override
	public boolean canBreakBlocks()
	{
		return MC.player.isOnGround();
	}

	public static double sqrt(Vec3d one, BlockPos two) {
		return Math.sqrt(Math.pow(one.getX() - two.getX(), 2) + Math.pow(one.getY() - two.getY(), 2) + Math.pow(one.getZ() - two.getZ(), 2));
	}
}
