package com.klapeks.colinker.bungee;

import java.util.HashMap;
import java.util.List;

import com.klapeks.colinker.Functions;
import com.klapeks.coserver.aConfig;
import com.klapeks.coserver.dCoserver;
import com.klapeks.funcs.dRSA;
import com.klapeks.funcs.uArrayMap;

import net.md_5.bungee.Util;
import net.md_5.bungee.api.ProxyServer;

public class ColinServer {

	public static int last_id = 0;
//	static HashMap<String, Integer> mode$last_id = new HashMap<>();
	static uArrayMap<String, ColinServer> mode$server = new uArrayMap<>();
	static HashMap<Integer, ColinServer> id$server = new HashMap<>();

//	static ColinServer makeServer(String mode, String ip, int port) {
//		String s = ip+":"+port;
//		if (ip$server.containsKey(s)) return ip$server.get(s);
//		return createServer(mode, ip, port);
//	}
	static ColinServer findServer(String mode, String ip, int mcport) {
		List<ColinServer> css = mode$server.get(mode);
		for (ColinServer cs : css) {
			if (cs.ip.equals(ip) && cs.mcport == mcport) return cs;
		}
		return null;
	}
	static ColinServer remakeServer(String mode, String ip, int mcport) {
		deleteServer(findServer(mode, ip, mcport));
		return createServer(mode, ip, mcport);
	}
	private static ColinServer createServer(String mode, String ip, int port) {
		ColinServer cs = new ColinServer(mode, ip, port);
		mode$server.addIn(mode, cs);
		id$server.put(cs.id, cs);
		return cs;
	}
	static ColinServer deleteServer(String mode, int id) {
//		String s = ip+":"+port;
		ColinServer cs = id$server.get(id);
		mode$server.remove(mode, cs);
		id$server.remove(id);
		return cs;
	}
	static ColinServer deleteServer(ColinServer cs) {
		if (cs==null) return cs;
		cs.onDisabling();
		mode$server.remove(cs.mode, cs);
		id$server.remove(cs.id);
		return cs;
	}
	
	static void recalculateServers() {
		uArrayMap<String, ColinServer> am = new uArrayMap<>();
		int maxonline = 0;
		for (String mode : mode$server.keySet()) {
			for (ColinServer cs : mode$server.get(mode)) {
				try {
					if (cs.sendData("isalive").equals("yesiamalive")) {
						maxonline += cs.maxonline;
						continue;
					}
				} catch (Throwable t) {
					if (aConfig.useDebugMsg) t.printStackTrace();
				}
				am.addIn(mode, cs);
			}
		}
		ColinConnector.max_online = maxonline;
		int i = 0;
		for (String mode : am.keySet()) {
			for (ColinServer cs : am.get(mode)) {
				deleteServer(cs);
				i++;
			}
		}
		if (i > 0) Functions.log("§e{0} §6servers was disabled due crash or something", i);
		am.clear();
	}
//	public static ColinServer getServerByIP(String ip, int port) {
//	return ip$server.get(ip+":"+port);
//}
	public static ColinServer getServerByID(int id) {
		return id$server.get(id);
	}
	
	final int mcport, id;
	public int rcport;
	final String mode, ip;
	boolean isEnabled = false;
	private ColinServer(String mode, String ip, int mcport) {
		this.ip = ip; this.mcport = mcport;
		this.mode = mode;
		this.id = ++last_id;
	}

	int maxonline = 0;
	void onEnabling(Object... args) {
		isEnabled = true;
		maxonline = args.length > 0 ? (Integer) args[0] : 0;
		ColinConnector.max_online += maxonline;
		
		ProxyServer.getInstance().getServers().put(mode.split("")[0]+id, ProxyServer.getInstance()
				.constructServerInfo(mode.split("")[0]+id, Util.getAddr(ip+":"+mcport), "mode:"+mode, false));
		
		Functions.log("§aServer §6" + id + "§7(§e"+ip+":"+mcport+"§f)§a was enabled and added as §b" + mode + "§a.§r");
		Functions.log("§aServer §9{0}§a was enabled", id);
	}
	
	public String getMode() {
		return mode;
	}
	
	void onDisabling() {
		isEnabled = false;
		ColinConnector.max_online -= maxonline;
		ProxyServer.getInstance().getServers().remove(mode.split("")[0]+id);
		Functions.log("§cServer §9{0}§c was disabled", id);
	}
	
	public String sendData(String cmd, Object... args) {
		cmd = dRSA.base64_encode(cmd);
		for (Object arg : args) {
			cmd += " " + dRSA.base64_encode(arg+"");
		}
		if (aConfig.useSecurity) {
			return dCoserver.securitySend(ip, rcport, "colinkertos " + cmd, false);
		} else {
			return dCoserver.send(ip, rcport, "colinkertos " + cmd, false);
		}
	}
	

}
