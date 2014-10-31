package com.gw.monitor.alphaenvmonitor.domain;

public class ServerStatus {
	private String server;
	private String conn;
	private Integer speed;
	private Integer slow;

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getConn() {
		return conn;
	}

	public void setConn(String conn) {
		this.conn = conn;
	}

	public Integer getSpeed() {
		return speed;
	}

	public void setSpeed(Integer speed) {
		this.speed = speed;
	}

	public Integer getSlow() {
		return slow;
	}

	public void setSlow(Integer slow) {
		this.slow = slow;
	}
}
