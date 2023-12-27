/*

 *



 */
package net.purefps.settings;

import java.util.function.Consumer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import net.purefps.PFPSClient;
import net.purefps.util.RotationUtils;
import net.purefps.util.RotationUtils.Rotation;

public final class FacingSetting extends EnumSetting<FacingSetting.Facing>
{
	private static final PFPSClient WURST = PFPSClient.INSTANCE;
	private static final MinecraftClient MC = PFPSClient.MC;

	private FacingSetting(String name, String description, Facing[] values,
		Facing selected)
	{
		super(name, description, values, selected);
	}

	public static FacingSetting withoutPacketSpam(String description)
	{
		return withoutPacketSpam("Facing", description, Facing.SERVER);
	}

	public static FacingSetting withoutPacketSpam(String name,
		String description, Facing selected)
	{
		Facing[] values = {Facing.OFF, Facing.SERVER, Facing.CLIENT};
		return new FacingSetting(name, description, values, selected);
	}

	public static FacingSetting withPacketSpam(String name, String description,
		Facing selected)
	{
		return new FacingSetting(name, description, Facing.values(), selected);
	}

	public enum Facing
	{
		OFF("Off", v -> {}),

		SERVER("Server-side",
			v -> WURST.getRotationFaker().faceVectorPacket(v)),

		CLIENT("Client-side",
			v -> WURST.getRotationFaker().faceVectorClient(v)),

		SPAM("Packet spam", v -> {
			Rotation rotation = RotationUtils.getNeededRotations(v);
			PlayerMoveC2SPacket.LookAndOnGround packet =
				new PlayerMoveC2SPacket.LookAndOnGround(rotation.getYaw(),
					rotation.getPitch(), MC.player.isOnGround());
			MC.player.networkHandler.sendPacket(packet);
		});

		private String name;
		private Consumer<Vec3d> face;

		private Facing(String name, Consumer<Vec3d> face)
		{
			this.name = name;
			this.face = face;
		}

		public void face(Vec3d v)
		{
			face.accept(v);
		}

		@Override
		public String toString()
		{
			return name;
		}
	}
}
