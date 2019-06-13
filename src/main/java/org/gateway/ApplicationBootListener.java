package org.gateway;

import java.net.InetAddress;

import org.apache.commons.dbcp.BasicDataSource;
import org.gateway.config.ApiConfig;
import org.gateway.config.ApplicationConfig;
import org.gateway.mapper.MySqlInfoMapper;
import org.gateway.thread.RefreshApiConfigThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Application启动事件监听处理
 * @author mawentao
 */
@Component
public class ApplicationBootListener implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {
	
	@Autowired
	ApplicationConfig applicationConfig;
	
	@Autowired
	MySqlInfoMapper mysqlInfoMapper;
	
	@Autowired
	BasicDataSource dataSource;
	
	@Autowired
	ApiConfig apiConfig;
	
	@Autowired
	RefreshApiConfigThread refreshApiConfigThread;
	
	private static final Logger logger = LoggerFactory.getLogger(ApplicationBootListener.class);
	
    public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event) {
		try {
			//1. 打印配置信息
			logger.info(applicationConfig.toString());
			
			//2. 打印启动信息
			// 容器
	    	EmbeddedServletContainer container = event.getEmbeddedServletContainer();
	    	int port = container.getPort();
	    	// 主机信息
	    	InetAddress localHost = InetAddress.getLocalHost();
			String host = localHost.getHostAddress();
	    	String info = "http://"+host+":"+port;
	    	logger.info(info);
	    	
	    	//3. DB信息
	    	info = "MySQL"+mysqlInfoMapper.get().getVersion()
	    			+"\t"+dataSource.getUrl()
	    			+"\t{user:"+dataSource.getUsername()+",passwd:"+dataSource.getPassword()+"}";
	    	logger.info(info);
	    	
	    	//4. 全量加载API配置
	    	apiConfig.loadAllFromDB();
	    	
	    	//5. 启动API配置全量自动更新线程
	    	refreshApiConfigThread.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
