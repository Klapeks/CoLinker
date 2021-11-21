package com.klapeks.colinker.api;

public interface CoServer {

	public static enum ServerState {
		LOADING(0),
		ENABLED(1),
		READY(2),
		DISABLING(9);
		int state;
		private ServerState(int state) {
			this.state = state;
		}
		public int getState() {
			return state;
		}
	}
	
	int getMaxPlayers();
	ServerState getState();
	String getMode();
	int getId();
}
