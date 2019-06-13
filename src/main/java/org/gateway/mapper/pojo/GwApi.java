package org.gateway.mapper.pojo;

import lombok.Data;

@Data
public class GwApi {
	
	private long id;
	
	private long appId;
	
	private String appName;
	
	private String frontPath;
	
	private String prePath;
	
	private String backPath;
	
	private int timeout;
	
	private int maxFlow;
	
	private String authkeys;
	
	private int isMock;
	
	private String mock;
	
	private String env;
	
	private String backurl;

	
	/**
	 * 设置GwApp的字段
	 * @param gwApp
	 */
	public void setGwApp(GwApp gwApp) {
		env = gwApp.getEnv();
		backurl = gwApp.getBackurl();
		appName = gwApp.getName();
		prePath = gwApp.getPrepath();
	}
}
