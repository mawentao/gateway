package org.gateway.mapper.pojo;

import lombok.Data;

@Data
public class MySqlInfo {
	
	/**
	 * mysql版本号
	 */
	private String version;

	/**
	 * mysql当前时间
	 */
	private String now;
	
}
