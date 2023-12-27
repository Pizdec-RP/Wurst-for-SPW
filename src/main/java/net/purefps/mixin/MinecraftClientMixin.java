/*

 *



 */
package net.purefps.mixin;

import java.io.File;
import java.util.UUID;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.WindowEventHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.session.ProfileKeys;
import net.minecraft.client.session.ProfileKeysImpl;
import net.minecraft.client.session.Session;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import net.purefps.PFPSClient;
import net.purefps.event.EventManager;
import net.purefps.events.LeftClickListener.LeftClickEvent;
import net.purefps.events.RightClickListener.RightClickEvent;
import net.purefps.mixinterface.IClientPlayerEntity;
import net.purefps.mixinterface.IClientPlayerInteractionManager;
import net.purefps.mixinterface.IMinecraftClient;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin
	extends ReentrantThreadExecutor<Runnable>
	implements WindowEventHandler, IMinecraftClient
{
	@Shadow
	@Final
	public File runDirectory;
	@Shadow
	public ClientPlayerInteractionManager interactionManager;
	@Shadow
	public ClientPlayerEntity player;
	@Shadow
	@Final
	private YggdrasilAuthenticationService authenticationService;
	
	private Session wurstSession;
	private ProfileKeysImpl wurstProfileKeys;
	
	private MinecraftClientMixin(PFPSClient wurst, String name)
	{
		super(name);
	}
	
	@Inject(at = @At(value = "FIELD",
		target = "Lnet/minecraft/client/MinecraftClient;crosshairTarget:Lnet/minecraft/util/hit/HitResult;",
		ordinal = 0), method = "doAttack()Z", cancellable = true)
	private void onDoAttack(CallbackInfoReturnable<Boolean> cir)
	{
		LeftClickEvent event = new LeftClickEvent();
		EventManager.fire(event);
		
		if(event.isCancelled())
			cir.setReturnValue(false);
	}
	
	@Inject(
		at = @At(value = "FIELD",
			target = "Lnet/minecraft/client/MinecraftClient;itemUseCooldown:I",
			ordinal = 0),
		method = "doItemUse()V",
		cancellable = true)
	private void onDoItemUse(CallbackInfo ci)
	{
		RightClickEvent event = new RightClickEvent();
		EventManager.fire(event);
		
		if(event.isCancelled())
			ci.cancel();
	}
	
	@Inject(at = @At("HEAD"), method = "doItemPick()V")
	private void onDoItemPick(CallbackInfo ci)
	{
		if(!PFPSClient.INSTANCE.isEnabled())
			return;
		
		HitResult hitResult = PFPSClient.MC.crosshairTarget;
		if(!(hitResult instanceof EntityHitResult eHitResult))
			return;
		
		PFPSClient.INSTANCE.getFriends().middleClick(eHitResult.getEntity());
	}
	
	@Inject(at = @At("HEAD"),
		method = "getSession()Lnet/minecraft/client/session/Session;",
		cancellable = true)
	private void onGetSession(CallbackInfoReturnable<Session> cir)
	{
		if(wurstSession != null)
			cir.setReturnValue(wurstSession);
	}
	
	@Inject(at = @At("RETURN"),
		method = "getGameProfile()Lcom/mojang/authlib/GameProfile;",
		cancellable = true)
	public void onGetGameProfile(CallbackInfoReturnable<GameProfile> cir)
	{
		if(wurstSession == null)
			return;
		
		GameProfile oldProfile = cir.getReturnValue();
		GameProfile newProfile = new GameProfile(wurstSession.getUuidOrNull(),
			wurstSession.getUsername());
		newProfile.getProperties().putAll(oldProfile.getProperties());
		cir.setReturnValue(newProfile);
	}
	
	@Inject(at = @At("HEAD"),
		method = "getProfileKeys()Lnet/minecraft/client/session/ProfileKeys;",
		cancellable = true)
	private void onGetProfileKeys(CallbackInfoReturnable<ProfileKeys> cir)
	{
		if(PFPSClient.INSTANCE.getOtfs().noChatReportsOtf.isActive())
			cir.setReturnValue(ProfileKeys.MISSING);
		
		if(wurstProfileKeys == null)
			return;
		
		cir.setReturnValue(wurstProfileKeys);
	}
	
	@Inject(at = @At("HEAD"),
		method = "isTelemetryEnabledByApi()Z",
		cancellable = true)
	private void onIsTelemetryEnabledByApi(CallbackInfoReturnable<Boolean> cir)
	{
		cir.setReturnValue(
			!PFPSClient.INSTANCE.getOtfs().noTelemetryOtf.isEnabled());
	}
	
	@Inject(at = @At("HEAD"),
		method = "isOptionalTelemetryEnabledByApi()Z",
		cancellable = true)
	private void onIsOptionalTelemetryEnabledByApi(
		CallbackInfoReturnable<Boolean> cir)
	{
		cir.setReturnValue(
			!PFPSClient.INSTANCE.getOtfs().noTelemetryOtf.isEnabled());
	}
	
	@Override
	public IClientPlayerEntity getPlayer()
	{
		return (IClientPlayerEntity)player;
	}
	
	@Override
	public IClientPlayerInteractionManager getInteractionManager()
	{
		return (IClientPlayerInteractionManager)interactionManager;
	}
	
	@Override
	public void setSession(Session session)
	{
		wurstSession = session;
		
		UserApiService userApiService = authenticationService
			.createUserApiService(session.getAccessToken());
		UUID uuid = wurstSession.getUuidOrNull();
		wurstProfileKeys =
			new ProfileKeysImpl(userApiService, uuid, runDirectory.toPath());
	}
}

