/*

 *



 */
package net.purefps;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.purefps.events.PostMotionListener;
import net.purefps.events.PreMotionListener;
import net.purefps.util.RotationUtils;

public final class RotationFaker
	implements PreMotionListener, PostMotionListener
{
	private boolean fakeRotation;
	private float serverYaw;
	private float serverPitch;
	private float realYaw;
	private float realPitch;

	@Override
	public void onPreMotion()
	{
		if(!fakeRotation)
			return;

		ClientPlayerEntity player = PFPSClient.MC.player;
		realYaw = player.getYaw();
		realPitch = player.getPitch();
		player.setYaw(serverYaw);
		player.setPitch(serverPitch);
	}

	@Override
	public void onPostMotion()
	{
		if(!fakeRotation)
			return;

		ClientPlayerEntity player = PFPSClient.MC.player;
		player.setYaw(realYaw);
		player.setPitch(realPitch);
		fakeRotation = false;
	}

	public void faceVectorPacket(Vec3d vec)
	{
		RotationUtils.Rotation needed = RotationUtils.getNeededRotations(vec);
		ClientPlayerEntity player = PFPSClient.MC.player;

		fakeRotation = true;
		serverYaw =
			RotationUtils.limitAngleChange(player.getYaw(), needed.getYaw());
		serverPitch = needed.getPitch();
	}

	public void faceVectorClient(Vec3d vec)
	{
		RotationUtils.Rotation needed = RotationUtils.getNeededRotations(vec);

		ClientPlayerEntity player = PFPSClient.MC.player;
		player.setYaw(
			RotationUtils.limitAngleChange(player.getYaw(), needed.getYaw()));
		player.setPitch(needed.getPitch());
	}

	public void faceVectorClientIgnorePitch(Vec3d vec)
	{
		RotationUtils.Rotation needed = RotationUtils.getNeededRotations(vec);

		ClientPlayerEntity player = PFPSClient.MC.player;
		PFPSClient.MC.player.setYaw(
			RotationUtils.limitAngleChange(player.getYaw(), needed.getYaw()));
	}

	public float getServerYaw()
	{
		return fakeRotation ? serverYaw : PFPSClient.MC.player.getYaw();
	}

	public float getServerPitch()
	{
		return fakeRotation ? serverPitch : PFPSClient.MC.player.getPitch();
	}
}
