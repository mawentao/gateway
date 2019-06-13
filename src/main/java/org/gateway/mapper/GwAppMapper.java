package org.gateway.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.gateway.mapper.pojo.GwApp;

@Mapper
public interface GwAppMapper {

	/**
	 * 获取mysql系统信息
	 * @return
	 */
	@Select("SELECT a.id,a.name,a.prepath,a.remark,b.name as env,b.url as backurl "+
			"FROM gw_app as a "+
			"LEFT JOIN gw_app_env as b ON b.app_id=a.id AND b.name=#{env} AND b.isdel=0 "+
			"WHERE a.status=1 AND a.isdel=0 AND b.name is not null")
	@Results(value = {})
	public List<GwApp> fetchAll(String env);
	
}
