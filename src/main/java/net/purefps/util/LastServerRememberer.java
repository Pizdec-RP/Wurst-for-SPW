/*

 *



 */
package net.purefps.util;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.purefps.PFPSClient;
import net.purefps.mixinterface.IMultiplayerScreen;

/**
 * Remembers the last server you joined to make the "Reconnect",
 * "AutoReconnect" and "Last Server" buttons work.
 */
public enum LastServerRememberer
{
	;

	private static ServerInfo lastServer;

	public static ServerInfo getLastServer()
	{
		return lastServer;
	}

	public static void setLastServer(ServerInfo server)
	{
		lastServer = server;
	}

	public static void joinLastServer(MultiplayerScreen mpScreen)
	{
		if(lastServer == null)
			return;

		((IMultiplayerScreen)mpScreen).connectToServer(lastServer);
	}

	public static void reconnect(Screen prevScreen)
	{
		if(lastServer == null)
			return;

		ConnectScreen.connect(prevScreen, PFPSClient.MC,
			ServerAddress.parse(lastServer.address), lastServer, false);
	}
}
