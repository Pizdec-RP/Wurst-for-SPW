/*

 *



 */
package net.purefps.other_features;

import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.purefps.DontBlock;
import net.purefps.SearchTags;
import net.purefps.events.ConnectionPacketOutputListener;
import net.purefps.mixin.CustomPayloadC2SPacketAccessor;
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
		
		if(!(event.getPacket() instanceof CustomPayloadC2SPacketAccessor))
			return;
		
		CustomPayloadC2SPacketAccessor packet =
			(CustomPayloadC2SPacketAccessor)event.getPacket();
		
		if(packet.getChannel().getNamespace().equals("minecraft")
			&& packet.getChannel().getPath().equals("register"))
			event.cancel();
		
		if(packet.getChannel().getNamespace().equals("minecraft")
			&& packet.getChannel().getPath().equals("brand"))
			event.setPacket(new CustomPayloadC2SPacket(
				CustomPayloadC2SPacket.BRAND,
				new PacketByteBuf(Unpooled.buffer()).writeString("vanilla")));
		
		if(packet.getChannel().getNamespace().equals("fabric"))
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
