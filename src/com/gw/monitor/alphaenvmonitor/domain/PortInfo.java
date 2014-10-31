package com.gw.monitor.alphaenvmonitor.domain;

import java.util.Map;

public class PortInfo {
	private Map<String, String> listenMap;
	private Map<String, String> queryMap;
	
	public Map<String, String> getListenMap() {
		return listenMap;
	}
	public void setListenMap(Map<String, String> listenMap) {
		this.listenMap = listenMap;
	}
	
	public Map<String, String> getQueryMap() {
		return queryMap;
	}
	public void setQueryMap(Map<String, String> queryMap) {
		this.queryMap = queryMap;
	}	
}
