package com.klapeks.colinker.api;

import java.util.function.Consumer;
import java.util.function.Function;

import com.klapeks.colinker.ColinHandler;
import com.klapeks.colinker.ColinServer;

public class CoLinkerAPI {
	public static CoServer getServer() {
		return ColinServer.getInstance();
	}
	public static enum Event {CONNECT, DISCONNECT, PRELOAD, INLOADING};
	public static void addConnectHandler(Consumer<Event> e) {
		ColinHandler.addConnectHandler(e);
	}
	
	public static void addHandler(String cmd, Function<String[], String> handler) {
		ColinHandler.addHandler(cmd, handler);
	}
}
