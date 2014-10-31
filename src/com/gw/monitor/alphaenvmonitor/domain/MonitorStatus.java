package com.gw.monitor.alphaenvmonitor.domain;

import java.util.List;

public class MonitorStatus {
	private List<ClientStatus> client;
	private List<ServerStatus> server;
	
	public List<ClientStatus> getClient() {
		return client;
	}
	public void setClient(List<ClientStatus> client) {
		this.client = client;
	}
	
	public List<ServerStatus> getServer() {
		return server;
	}
	public void setServer(List<ServerStatus> server) {
		this.server = server;
	}
}



