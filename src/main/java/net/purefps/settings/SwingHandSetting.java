/*

 *



 */
package net.purefps.settings;

import java.util.function.Consumer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.util.Hand;
import net.purefps.PFPSClient;

public final class SwingHandSetting
	extends EnumSetting<SwingHandSetting.SwingHand>
{
	private static final MinecraftClient MC = PFPSClient.MC;

	public SwingHandSetting(String description)
	{
		super("Swing hand", description, SwingHand.values(), SwingHand.SERVER);
	}

	public SwingHandSetting(String description, SwingHand selected)
	{
		super("Swing hand", description, SwingHand.values(), selected);
	}

	public SwingHandSetting(String name, String description, SwingHand selected)
	{
		super(name, description, SwingHand.values(), selected);
	}

	public enum SwingHand
	{
		OFF("Off", hand -> {}),

		SERVER("Server-side",
			hand -> MC.player.networkHandler
				.sendPacket(new HandSwingC2SPacket(hand))),

		CLIENT("Client-side", hand -> MC.player.swingHand(hand));

		private String name;
		private Consumer<Hand> swing;

		private SwingHand(String name, Consumer<Hand> swing)
		{
			this.name = name;
			this.swing = swing;
		}

		public void swing(Hand hand)
		{
			swing.accept(hand);
		}

		@Override
		public String toString()
		{
			return name;
		}
	}
}
