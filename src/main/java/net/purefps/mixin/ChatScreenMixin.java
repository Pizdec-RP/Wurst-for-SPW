/*

 *



 */
package net.purefps.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.purefps.PFPSClient;
import net.purefps.event.EventManager;
import net.purefps.events.ChatOutputListener.ChatOutputEvent;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin extends Screen
{
	@Shadow
	protected TextFieldWidget chatField;

	private ChatScreenMixin(PFPSClient wurst, Text title)
	{
		super(title);
	}

	@Inject(at = @At("TAIL"), method = "init()V")
	protected void onInit(CallbackInfo ci)
	{
		if(PFPSClient.INSTANCE.getHax().infiniChatHack.isEnabled())
			chatField.setMaxLength(Integer.MAX_VALUE);
	}

	@Inject(at = @At("HEAD"),
		method = "sendMessage(Ljava/lang/String;Z)Z",
		cancellable = true)
	public void onSendMessage(String message, boolean addToHistory,
		CallbackInfoReturnable<Boolean> cir)
	{
		if((message = normalize(message)).isEmpty())
			return;

		ChatOutputEvent event = new ChatOutputEvent(message);
		EventManager.fire(event);

		if(event.isCancelled())
		{
			cir.setReturnValue(true);
			return;
		}

		if(!event.isModified())
			return;

		String newMessage = event.getMessage();
		if(addToHistory)
			client.inGameHud.getChatHud().addToMessageHistory(newMessage);

		if(newMessage.startsWith("/"))
			client.player.networkHandler
				.sendChatCommand(newMessage.substring(1));
		else
			client.player.networkHandler.sendChatMessage(newMessage);

		cir.setReturnValue(true);
	}

	@Shadow
	public abstract String normalize(String chatText);
}
