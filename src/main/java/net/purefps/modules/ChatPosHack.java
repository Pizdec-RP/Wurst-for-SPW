// 
// Decompiled by Procyon v0.6.0
// 

package net.purefps.modules;

import net.purefps.util.ChatUtils;
import net.purefps.Category;
import net.purefps.events.UpdateListener;
import net.purefps.module.Hack;

public class ChatPosHack extends Hack implements UpdateListener
{
    public ChatPosHack() {
        super("ChatPosHack");
        this.setCategory(Category.OTHER);
    }
    
    public void onEnable() {
        ChatPosHack.EVENTS.add(UpdateListener.class, this);
    }
    
    public void onDisable() {
        ChatPosHack.EVENTS.remove(UpdateListener.class, this);
    }
    
    @Override
    public void onUpdate() {
        ChatUtils.message(ChatPosHack.MC.player.getPos().toString());
    }
}
