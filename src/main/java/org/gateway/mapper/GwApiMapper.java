package org.gateway.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.gateway.mapper.pojo.GwApi;

@Mapper
public interface GwApiMapper {

	/**
	 * 获取mysql系统信息
	 * @return
	 */
	@Select("SELECT a.id,a.app_id,a.front_path,a.back_path,a.timeout,a.max_flow,a.authkeys,a.is_mock,a.mock,"+
			"b.name as appName,b.prepath "+
			"FROM gw_api as a "+
			"LEFT JOIN gw_app as b ON a.app_id=b.id "+
			"WHERE a.status=1 AND a.isdel=0 AND b.status=1 AND b.isdel=0")
	@Results(value = {
			@Result(property = "appId", column = "app_id"),
			@Result(property = "frontPath", column = "front_path"),
			@Result(property = "prePath", column = "prepath"),
			@Result(property = "backPath", column = "back_path"),
			@Result(property = "timeout", column = "timeout"),
			@Result(property = "maxFlow", column = "max_flow"),
			@Result(property = "isMock", column = "is_mock"),
	})
	public List<GwApi> fetchAll();
	
}
