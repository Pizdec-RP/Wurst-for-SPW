//
// Decompiled by Procyon v0.6.0
//

package net.purefps.modules;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.network.packet.Packet;
import net.purefps.Category;
import net.purefps.Feature;
import net.purefps.SearchTags;
import net.purefps.events.PacketInputListener;
import net.purefps.events.PacketOutputListener;
import net.purefps.module.Hack;
import net.purefps.settings.SliderSetting;

@SearchTags({ "packet print" })
public class PacketPrintHack extends Hack implements PacketOutputListener, PacketInputListener
{
    public List<Packet<?>> input;
    public List<Packet<?>> output;
    private final SliderSetting pakcap;

    public PacketPrintHack() {
        super("PacketPrint");
        this.input = new CopyOnWriteArrayList<>();
        this.output = new CopyOnWriteArrayList<>();
        this.pakcap = new SliderSetting("capacity of packets", "", 10.0, 0.0, 40.0, 1.0, SliderSetting.ValueDisplay.INTEGER.withLabel(0.0, "nigga why?"));
        this.setCategory(Category.OTHER);
    }

    @Override
	public void onEnable() {
        Feature.EVENTS.add(PacketOutputListener.class, this);
        Feature.EVENTS.add(PacketInputListener.class, this);
    }

    @Override
	public void onDisable() {
        Feature.EVENTS.remove(PacketOutputListener.class, this);
        Feature.EVENTS.remove(PacketInputListener.class, this);
    }

    @Override
    public void onSentPacket(final PacketOutputEvent event) {
        this.output.add(event.getPacket());
        if (this.output.size() > this.pakcap.getValue()) {
            this.output.remove(0);
        }
    }

    @Override
    public void onReceivedPacket(final PacketInputEvent event) {
        this.input.add(event.getPacket());
        if (this.input.size() > this.pakcap.getValue()) {
            this.input.remove(0);
        }
    }
}
