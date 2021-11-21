package com.klapeks.colinker.bungee;

import com.klapeks.colinker.Functions;
import com.klapeks.colinker.bungee.ColinConnector.RandomFinder;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class EventListeners {

	public static class PingListener implements Listener {
		@EventHandler(priority=EventPriority.HIGH)
		public void onPrPing(ProxyPingEvent e) {
			ServerPing sp = e.getResponse();
			if (ColinConnector.max_online <= 0) {
				sp.getVersion().setName("§c" + Config.lang.alldisabled);
				sp.getVersion().setProtocol(-1);
			} else {
				sp.getPlayers().setMax(ColinConnector.max_online);
			}
			e.setResponse(sp);
		}
	}
	public static class ConnectListener implements Listener {

		@SuppressWarnings("deprecation")
		@EventHandler
		public void onKick(ServerKickEvent e) {
			String msg = e.getKickReason();
			if (msg.contains("%em%")) {
				msg = msg.split("%em%")[1];
				String f1 = msg.split(":")[0];
				msg = msg.replaceFirst(f1+":", "");
				switch (f1) {
				case "err_connect": {
					e.setKickReason("§cConnect error\n"
							+ "§4Server isn't responding\n\n"
							+ "§7Please inform the administration\n"
							+ "§7about the error with code §f" + msg);
					
					ServerInfo si = ProxyServer.getInstance().getServerInfo(msg);
					Functions.log("Error code: " + msg + "\nServer: " + si.getMotd() + " $ " + si.getAddress().toString());
					return;
				}
				default:
					break;
				}
			}
		}
		@EventHandler
		public void onServerChange(ServerConnectEvent e) {
			String name = e.getTarget().getName();
			if (name.equals("__colinker__")) {
				ColinServer cs = RandomFinder.getHub();
				if (cs==null) {
					e.setCancelled(true);
					e.getPlayer().disconnect(new TextComponent("§c"+Config.lang.notfound));
					return;
				}
				String servername = cs.mode.split("")[0] + cs.id;
				e.setTarget(ProxyServer.getInstance().getServers().get(servername));
				return;
			}
			if (name.startsWith("_mode:")) {
				name = name.replaceFirst("_mode:", "");
				if (name.startsWith(" ")) name = name.replaceFirst(" ", "");
				ColinServer cs = RandomFinder.getServer(name);
				if (cs==null) {
					e.setCancelled(true);
					e.getPlayer().disconnect(new TextComponent("§c"+Config.lang.notfound));
					return;
				}
				String servername = cs.mode.split("")[0] + cs.id;
				e.setTarget(ProxyServer.getInstance().getServers().get(servername));
				return;
			}
			
//			e.getPlayer().sendMessage(e.getPlayer().isConnected() + " - " + e.getPlayer().getServer());
//			e.getPlayer().sendMessage("Connect to " + e.getTarget().getName() + " + " + e.getTarget().getAddress() + "");
//			e.getPlayer().sendMessage("§btest1 - join - server connect (bungee pl)");
		}
	}
	
}
