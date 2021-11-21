package com.klapeks.colinker.bungee;

import java.net.Socket;
import java.util.function.BiFunction;

import com.klapeks.colinker.Functions;
import com.klapeks.colinker.bungee.ColinConnector.RandomFinder;
import com.klapeks.coserver.aConfig;
import com.klapeks.coserver.plugin.bungee.SuperCoServer;
import com.klapeks.funcs.dRSA;

public class ColinHandler {
	
	public static void __init() {
		BiFunction<Socket, String, String> handler = (socket, request) -> {
			String[] args = doArgs(request);
			switch (args[0]) {
			case "loading": {
				String mode = args[1];
				int rcport = Integer.parseInt(args[2]);
				int mcport = Integer.parseInt(args[3]);
				MainBungee.plugin.getLogger().info(socket.getInetAddress().getHostAddress()+":"+mcport);
				ColinServer cs = ColinServer.remakeServer(mode, socket.getInetAddress().getHostAddress()+"", mcport);
				cs.rcport = rcport;
				Functions.log("§3Server §9{0}§b({1})§3 is loading now. Its id is §9{2}", cs.ip + ":" + cs.mcport +"/"+ cs.rcport, cs.mode, cs.id);
				return "" + cs.id + "";
			}
			case "enabled": {
				int id = Integer.parseInt(args[1]);
				ColinServer cs = ColinServer.getServerByID(id);
				if (cs==null || cs.id != id) return "fail";
				cs.onEnabling(Integer.parseInt(args[2]));
				return "ok";
			}
			case "disabled": {
				int id = Integer.parseInt(args[1]);
				ColinServer cs = ColinServer.getServerByID(id);
				if (cs==null || cs.id != id) return "fail";
				ColinServer.deleteServer(cs);
				return "done";
			}
			case "playerconnect": {
				String server_mode = args[1];
				String thisid = args[2];
				ColinServer cs;
				if (server_mode.startsWith("mode:")) {
					cs = RandomFinder.getServer(server_mode.replaceFirst("mode:", ""), thisid);
				} else {
					try {
						String s = server_mode.substring(1);
						cs = ColinServer.getServerByID(Integer.parseInt(s));
					} catch (Throwable t) {
						return "$fail";
					}
				}
				if (cs==null) return "$fail";
				return cs.mode.split("")[0] + cs.id; 
			}
			default:
				break;
			}
			return "";
		};
		if (aConfig.useSecurity) {
			SuperCoServer.BungeeCoserv.addSocketSecurityHandler("colinker", handler);
		} else {
			SuperCoServer.BungeeCoserv.addSocketHandler("colinker", handler);
		}
	}
	
	
	
	private static String[] doArgs(String req) {
		String[] ss = req.split(" ");
		for (int i = 0; i < ss.length; i++) {
			try { 
				ss[i] = dRSA.base64_decode(ss[i]);
			} 
			catch (Throwable r) {}
		}
		return ss;
	}
}
