package org.gateway.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.gateway.mapper.pojo.MySqlInfo;

@Mapper
public interface MySqlInfoMapper {

	/**
	 * 获取mysql系统信息
	 * @return
	 */
	@Select("SELECT version() as version, now() as now FROM dual")
	@Results(value = { 
			@Result(property = "version", column = "version"),
			@Result(property = "now", column = "now")
	})
	public MySqlInfo get();

}
