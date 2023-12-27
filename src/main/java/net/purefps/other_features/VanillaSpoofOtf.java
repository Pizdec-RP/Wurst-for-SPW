/*

 *



 */
package net.purefps.other_features;

import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;
import net.purefps.DontBlock;
import net.purefps.SearchTags;
import net.purefps.events.ConnectionPacketOutputListener;
import net.purefps.other_feature.OtherFeature;
import net.purefps.settings.CheckboxSetting;

@DontBlock
@SearchTags({"vanilla spoof", "AntiFabric", "anti fabric", "LibHatesMods",
	"HackedServer"})
public final class VanillaSpoofOtf extends OtherFeature
	implements ConnectionPacketOutputListener
{
	private final CheckboxSetting spoof =
		new CheckboxSetting("Spoof Vanilla", false);

	public VanillaSpoofOtf()
	{
		super("VanillaSpoof",
			"Bypasses anti-Fabric plugins by pretending to be a vanilla client.");
		addSetting(spoof);

		EVENTS.add(ConnectionPacketOutputListener.class, this);
	}

	@Override
	public void onSentConnectionPacket(ConnectionPacketOutputEvent event)
	{
		if(!spoof.isChecked())
			return;
		
		if(!(event.getPacket() instanceof CustomPayloadC2SPacket packet))
			return;
		
		Identifier channel = packet.payload().id();
		
		if(channel.getNamespace().equals("minecraft")
			&& channel.getPath().equals("register"))
			event.cancel();
			
		// Apparently the Minecraft client no longer sends its brand to the
		// server as of 23w31a
		
		// if(packet.getChannel().getNamespace().equals("minecraft")
		// && packet.getChannel().getPath().equals("brand"))
		// event.setPacket(new CustomPayloadC2SPacket(
		// CustomPayloadC2SPacket.BRAND,
		// new PacketByteBuf(Unpooled.buffer()).writeString("vanilla")));
		
		if(channel.getNamespace().equals("fabric"))
			event.cancel();
	}

	@Override
	public boolean isEnabled()
	{
		return spoof.isChecked();
	}

	@Override
	public String getPrimaryAction()
	{
		return isEnabled() ? "Disable" : "Enable";
	}

	@Override
	public void doPrimaryAction()
	{
		spoof.setChecked(!spoof.isChecked());
	}
}
