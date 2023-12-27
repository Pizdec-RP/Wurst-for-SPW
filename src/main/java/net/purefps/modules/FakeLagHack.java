//
// Decompiled by Procyon v0.6.0
//

package net.purefps.modules;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.network.packet.Packet;
import net.purefps.Category;
import net.purefps.Feature;
import net.purefps.SearchTags;
import net.purefps.events.PacketOutputListener;
import net.purefps.events.UpdateListener;
import net.purefps.module.DontSaveState;
import net.purefps.module.Hack;
import net.purefps.settings.SliderSetting;

@DontSaveState
@SearchTags({ "fake lag" })
public final class FakeLagHack extends Hack implements UpdateListener, PacketOutputListener
{
    private final SliderSetting min;
    private final SliderSetting max;
    private final SliderSetting pakcap;
    private final List<Packet<?>> packets;
    private final List<Packet<?>> resended;
    private int curtimeout;
    private static Random random;

    public FakeLagHack() {
        super("Fake lag");
        this.min = new SliderSetting("min limit ms", "", 0.0, 0.0, 40.0, 1.0, SliderSetting.ValueDisplay.INTEGER.withLabel(0.0, "min"));
        this.max = new SliderSetting("max limit ms", "", 0.0, 0.0, 40.0, 1.0, SliderSetting.ValueDisplay.INTEGER.withLabel(0.0, "min"));
        this.pakcap = new SliderSetting("max lagged packet capacity", "", 20.0, 0.0, 100.0, 1.0, SliderSetting.ValueDisplay.INTEGER.withLabel(0.0, "nigga why?"));
        this.packets = new CopyOnWriteArrayList<>();
        this.resended = new CopyOnWriteArrayList<>();
        this.curtimeout = 0;
        this.setCategory(Category.MOVEMENT);
        this.addSetting(this.min);
        this.addSetting(this.max);
        this.addSetting(this.pakcap);
    }

    @Override
    public String getRenderName() {
        return "Fakelag(" + this.curtimeout + ") capt(" + this.packets.size() + ") rs(" + this.resended.size();
    }

    @Override
	public void onEnable() {
        Feature.EVENTS.add(UpdateListener.class, this);
        Feature.EVENTS.add(PacketOutputListener.class, this);
    }

    public static int rndi(final int min, final int max) {
        return FakeLagHack.random.nextInt(max - min + 1) + min;
    }

    @Override
	public void onDisable() {
        Feature.EVENTS.remove(UpdateListener.class, this);
        Feature.EVENTS.remove(PacketOutputListener.class, this);
        packets.forEach(p -> MC.player.networkHandler.sendPacket(p));
        this.packets.clear();
    }

    @Override
    public void onUpdate() {
        final int curtimeout = this.curtimeout - 1;
        this.curtimeout = curtimeout;
        if (curtimeout <= 0) {
            final Iterator<Packet<?>> iterator = this.packets.iterator();
            Packet<?> p = null;
            while (iterator.hasNext()) {
                p = iterator.next();
                this.resended.add(p);
                if (this.resended.size() > 100) {
                    this.resended.remove(0);
                }
            }
            packets.forEach(p1 -> MC.player.networkHandler.sendPacket(p1));
            this.packets.clear();
            this.curtimeout = rndi(this.min.getValueI(), this.max.getValueI());
        }
    }

    @Override
    public void onSentPacket(final PacketOutputEvent event) {
        final Packet<?> packet = event.getPacket();
        if (this.resended.contains(packet)) {
            return;
        }
        if (this.packets.size() >= this.pakcap.getValueI()) {
            this.curtimeout = 0;
        }
        event.cancel();
        this.packets.add(packet);
    }

    static {
        FakeLagHack.random = new Random(1488L);
    }
}
