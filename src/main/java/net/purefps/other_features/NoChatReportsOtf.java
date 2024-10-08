/*

 *



 */
package net.purefps.other_features;

import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.client.gui.hud.MessageIndicator.Icon;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.encryption.ClientPlayerSession;
import net.minecraft.network.message.MessageChain;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import net.purefps.Category;
import net.purefps.DontBlock;
import net.purefps.PFPSClient;
import net.purefps.SearchTags;
import net.purefps.events.UpdateListener;
import net.purefps.other_feature.OtherFeature;
import net.purefps.settings.CheckboxSetting;
import net.purefps.util.ChatUtils;

@DontBlock
@SearchTags({"no chat reports", "NoEncryption", "no encryption",
	"NoChatSigning", "no chat signing"})
public final class NoChatReportsOtf extends OtherFeature
	implements UpdateListener
{
	private final CheckboxSetting disableSignatures =
		new CheckboxSetting("Disable signatures", true)
		{
			@Override
			public void update()
			{
				EVENTS.add(UpdateListener.class, NoChatReportsOtf.this);
			}
		};

	public NoChatReportsOtf()
	{
		super("NoChatReports", "description.purefps.other_feature.nochatreports");
		addSetting(disableSignatures);

		ClientLoginConnectionEvents.INIT.register(this::onLoginStart);
	}

	@Override
	public void onUpdate()
	{
		ClientPlayNetworkHandler netHandler = MC.getNetworkHandler();
		if(netHandler == null)
			return;

		if(isActive())
		{
			netHandler.session = null;
			netHandler.messagePacker = MessageChain.Packer.NONE;

		}else if(netHandler.session == null)
			MC.getProfileKeys().fetchKeyPair()
				.thenAcceptAsync(optional -> optional
					.ifPresent(profileKeys -> netHandler.session =
						ClientPlayerSession.create(profileKeys)),
					MC);

		EVENTS.remove(UpdateListener.class, this);
	}

	private void onLoginStart(ClientLoginNetworkHandler handler,
		MinecraftClient client)
	{
		EVENTS.add(UpdateListener.class, NoChatReportsOtf.this);
	}

	public MessageIndicator modifyIndicator(Text message,
		MessageSignatureData signature, MessageIndicator indicator)
	{
		if(!PFPSClient.INSTANCE.isEnabled() || MC.isInSingleplayer() || indicator != null || signature == null)
			return indicator;

		return new MessageIndicator(0xE84F58, Icon.CHAT_MODIFIED,
			Text.literal(ChatUtils.WURST_PREFIX + "\u00a7cReportable\u00a7r - ")
				.append(Text.translatable(
					"description.purefps.nochatreports.message_is_reportable")),
			"Reportable");
	}

	@Override
	public boolean isEnabled()
	{
		return disableSignatures.isChecked();
	}

	public boolean isActive()
	{
		return isEnabled() && PFPSClient.INSTANCE.isEnabled()
			&& !MC.isInSingleplayer();
	}

	@Override
	public String getPrimaryAction()
	{
		return WURST.translate("button.wurst.nochatreports."
			+ (isEnabled() ? "re-enable_signatures" : "disable_signatures"));
	}

	@Override
	public void doPrimaryAction()
	{
		disableSignatures.setChecked(!disableSignatures.isChecked());
	}

	@Override
	public Category getCategory()
	{
		return Category.CHAT;
	}

	// See ChatHudMixin, ClientPlayNetworkHandlerMixin.onOnServerMetadata(),
	// MinecraftClientMixin.onGetProfileKeys()
}
