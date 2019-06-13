package org.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 应用配置
 * @author mawentao
 *
 */
@Component
@Data
public class ApplicationConfig {
	
	@Value("${env}")
	private String env;
	
	@Value("${api_config_update_interval_s}")
	private int apiConfigUpdateIntervalS;

	@Value("${spring.application.name}")
	private String serverName;
}
