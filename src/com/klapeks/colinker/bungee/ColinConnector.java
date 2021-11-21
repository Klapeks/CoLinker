package com.klapeks.colinker.bungee;

import java.util.List;

import com.klapeks.colinker.Functions;
import com.klapeks.coserver.dFunctions;

public class ColinConnector {
	
	static int max_online = 0;
	
	public static class RandomFinder {
		public static ColinServer getServer(String mode, String... exclude_id) {
			@SuppressWarnings("unchecked")
			List<ColinServer> css = (List<ColinServer>) ColinServer.mode$server.get(mode).clone();
			ColinServer cs = null;
			while(css.size() > 0) {
				cs = css.get(dFunctions.getRandom(1, css.size())-1);
				if (Functions.contains(exclude_id, cs.id+"")) {
					css.remove(cs);
				} else return cs;
			}
			return null;
		}

		public static ColinServer getHub(String... exclude_id) {
			ColinServer cs = getServer(Config.hubmode, exclude_id);
			if (cs==null) {
				for (String mode : ColinServer.mode$server.keySet()) {
					cs = getServer(mode, exclude_id);
					if (cs!=null) return cs;
				}
			}
			return cs;
		}
	}
}
