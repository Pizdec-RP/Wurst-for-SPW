//
// Decompiled by Procyon v0.6.0
//

package net.purefps.modules;

import net.purefps.Category;
import net.purefps.Feature;
import net.purefps.events.UpdateListener;
import net.purefps.module.Hack;
import net.purefps.util.ChatUtils;

public class ChatPosHack extends Hack implements UpdateListener
{
    public ChatPosHack() {
        super("ChatPosHack");
        this.setCategory(Category.OTHER);
    }

    @Override
	public void onEnable() {
        Feature.EVENTS.add(UpdateListener.class, this);
    }

    @Override
	public void onDisable() {
        Feature.EVENTS.remove(UpdateListener.class, this);
    }

    @Override
    public void onUpdate() {
        ChatUtils.message(Feature.MC.player.getPos().toString());
    }
}
