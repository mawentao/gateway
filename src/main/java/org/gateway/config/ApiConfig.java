package org.gateway.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gateway.mapper.GwApiMapper;
import org.gateway.mapper.GwAppMapper;
import org.gateway.mapper.pojo.GwApi;
import org.gateway.mapper.pojo.GwApp;
import org.gateway.pojo.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * API配置
 * @author mawentao
 */
@Service
public class ApiConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(ApiConfig.class);
	
	@Autowired
	ApplicationConfig applicationConfig;
	
	@Autowired
	GwAppMapper gwAppMapper;
	
	@Autowired
	GwApiMapper gwApiMapper;

	// 双buffer切换（读写分离）
	Map<String, Api> apiMap0;
	Map<String, Api> apiMap1;
	int idx = 0;   //!< 当前使用的map
	boolean inLoading = false;
	
	public ApiConfig() {
		apiMap0 = new HashMap<String, Api>(1024*1024);
		apiMap1 = new HashMap<String, Api>(1024*1024);
		idx = 0;
	}
	
	/**
	 * 获取当前使用的map
	 * @return
	 */
	private Map<String, Api> getActiveMap() {
		return idx==0 ? apiMap0 : apiMap1;
	}
	
	/**
	 * 获取当前空闲的map
	 * @return
	 */
	private Map<String, Api> getIdleMap() {
		return idx==0 ? apiMap1 : apiMap0;
	}
	
	/**
	 * 根据path获取API配置
	 * @param path
	 * @return
	 */
	public Api get(String path) {
		Map<String, Api> apiMap = getActiveMap();
		return apiMap.get(path);
	}
	
	/**
	 * 返回API配置记录条数
	 * @return
	 */
	public int size() {
		return getActiveMap().size();
	}

	/**
	 * 从DB加载全部API配置
	 */
	public void loadAllFromDB() {
		if (inLoading) return;
		inLoading = true;

		// 环境定义
		String env = applicationConfig.getEnv();
		Map<String, Api> idlemap = getIdleMap();
		idlemap.clear();
		
		//1. 加载网关应用
		Map<Long, Integer> appMap = new HashMap<Long, Integer>();
		List<GwApp> gwApps = gwAppMapper.fetchAll(env);
		for (int i=0; i<gwApps.size(); ++i) {
			long appId = gwApps.get(i).getId();
			appMap.put(appId, i);
		}
		
		//2. 加载API列表
		List<GwApi> apis = gwApiMapper.fetchAll();
		for (GwApi gwApi : apis) {
			long appId = gwApi.getAppId();
			if (appMap.containsKey(appId)) {
				//2.1 拼装完整的GwAPI
				int idx = appMap.get(appId);
				GwApp gwApp = gwApps.get(idx);
				gwApi.setGwApp(gwApp);
				//2.2 创建API实例
				Api api = Api.createByGwApi(gwApi);
				//2.3 添加到idlemap
				idlemap.put(api.getApi(), api);
//				System.err.println(gwApi);
//				System.err.println(api);
				logger.debug(api.toString());
			}
		}
		logger.info("load_api_config_success[count:"+idlemap.size()+"]");
		
		//3. 切换buffer
		idx = 1 - idx;
		
		inLoading = false;
	}
	
	@Override
	public String toString() {
		Map<String, Api> apiMap = getActiveMap();
		String res = "ApiConfig:{size:"+apiMap.size()+"}";
		return res;
	}
}
