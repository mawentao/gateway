package org.gateway.mapper.pojo;

import lombok.Data;

@Data
public class GwApp {

	private int id;
	
	private String name;
	
	private String prepath;
	
	private String remark;
	
	private String env;
	
	private String backurl;
}
