package org.gateway.thread;

import org.gateway.config.ApiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 全量更新API配置线程，每隔一段时间从DB拉取一次路由配置
 * @author mawentao
 */
@Component
public class RefreshApiConfigThread extends Thread {

	private static final Logger logger = LoggerFactory.getLogger(RefreshApiConfigThread.class);
	
	// 自动更新的时间间隔
	@Value("${api_config_update_interval_s}")
	int updateIntervalS;
	
	@Autowired
	ApiConfig apiConfig;
	
	public void run() {
		logger.info("start-refresh-config-thread");
		while(true) {
			try {
				sleep(updateIntervalS*1000);
				apiConfig.loadAllFromDB();
			} catch (InterruptedException e) {
				logger.warn(e.getMessage());
			}
		}
	}
}
