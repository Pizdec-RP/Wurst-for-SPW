/*

 *



 */
package net.purefps.hud;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.math.Vec3d;
import net.purefps.PFPSClient;
import net.purefps.clickgui.ClickGui;
import net.purefps.clickgui.screens.ClickGuiScreen;
import net.purefps.events.GUIRenderListener;
import net.purefps.modules.PacketPrintHack;

public final class IngameHUD implements GUIRenderListener
{
	private final HackListHUD hackList = new HackListHUD();
	private TabGui tabGui;

	@Override
	public void onRenderGUI(DrawContext context, float partialTicks)
	{
		if(!PFPSClient.INSTANCE.isEnabled())
			return;

		if(tabGui == null)
			tabGui = new TabGui();

		boolean blend = GL11.glGetBoolean(GL11.GL_BLEND);
		ClickGui clickGui = PFPSClient.INSTANCE.getGui();

		// GL settings
		GL11.glDisable(GL11.GL_CULL_FACE);

		clickGui.updateColors();

		//wurstLogo.render(context);
		hackList.render(context, partialTicks);
		tabGui.render(context, partialTicks);

		final PacketPrintHack pp = PFPSClient.INSTANCE.getHax().packetPrintHack;
        int y = 0;
        final int x = PFPSClient.MC.getWindow().getScaledWidth();
        final TextRenderer tr = PFPSClient.MC.textRenderer;
        if (pp.isEnabled()) {
            for (Packet<?> p : pp.input) {
                final String name = p.getClass().getSimpleName();
                context.drawText(tr, name, x - tr.getWidth(name), y, -241, false);
                final int n = y;
                y = n + 9;
            }
            for (Packet<?> p : pp.output) {
                final String name = p.getClass().getSimpleName();
                context.drawText(tr, name, x - tr.getWidth(name), y, -16776961, false);
                final int n2 = y;
                y = n2 + 9;
            }
        }
        final Vec3d pos = PFPSClient.MC.player.getPos();
        final String spos = String.format("%.2f", pos.x) + " " + String.format("%.2f", pos.y) + " " + String.format("%.2f", pos.z);
        final String s = spos;
        final int n3 = PFPSClient.MC.getWindow().getScaledWidth() / 2 - tr.getWidth(spos) / 2;
        context.drawText(tr, s, n3, tr.fontHeight + 1, -1, true);

		// pinned windows
		if(!(PFPSClient.MC.currentScreen instanceof ClickGuiScreen))
			clickGui.renderPinnedWindows(context, partialTicks);

		// GL resets
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		RenderSystem.setShaderColor(1, 1, 1, 1);

		if(blend)
			GL11.glEnable(GL11.GL_BLEND);
		else
			GL11.glDisable(GL11.GL_BLEND);
	}

	public HackListHUD getHackList()
	{
		return hackList;
	}
}
