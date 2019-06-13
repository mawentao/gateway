package org.gateway.pojo;

import java.util.ArrayList;
import java.util.List;

import org.gateway.mapper.pojo.GwApi;

import lombok.Data;

/**
 * API实体
 * @author mawentao
 */
@Data
public class Api {

	private String api;

	private String uri;
	
	private int timeout;
	
	private int maxFlow;
	
	private List<String> authKeys;
	
	private boolean mock;
	
	private String mockData;
	
	public Api() {
		authKeys = new ArrayList<String>();
	}
	
	
	public static Api createByGwApi(GwApi gwApi) {
		Api instance = new Api();
		// 网关接口path
		String gwpath = appendPathPrefix(gwApi.getPrePath()) + 
						appendPathPrefix(gwApi.getFrontPath());
		instance.setApi(gwpath);
		// 网关转发url
		String gwurl = gwApi.getBackurl() + 
						appendPathPrefix(gwApi.getBackPath());
		instance.setUri(gwurl);
		// 鉴权配置
		instance.authKeys.clear();
		String authKeys = gwApi.getAuthkeys();
		String[] keys = authKeys.split(",");
		for (String authKey : keys) {
			authKey.split("[ |\t]");
			instance.authKeys.add(authKey.toLowerCase());
		}
		// 限流阈值
		instance.setMaxFlow(gwApi.getMaxFlow());
		// mock
		instance.setMock(gwApi.getIsMock()==1);
		instance.setMockData(gwApi.getMock());
		// 超时时间
		instance.setTimeout(gwApi.getTimeout());
		
		return instance;
	}
	
	/**
	 * 强制加上/前缀
	 * @param path
	 * @return
	 */
	private static String appendPathPrefix(String path) {
		if (!path.startsWith("/")) return "/"+path;
		return path;
	}
}
