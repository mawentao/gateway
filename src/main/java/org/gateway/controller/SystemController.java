package org.gateway.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.gateway.config.ApiConfig;
import org.gateway.config.ApplicationConfig;
import org.gateway.config.BaseController;
import org.gateway.pojo.Api;
import org.gateway.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Stopwatch;

/**
 *
 * 系统接口，不含具体业务处理，一般提供给运维使用
 * 禁止配置“/system”前缀
 * @author admin
 * @since 2019-06-11 15:49
 *
 */

@RestController
@RequestMapping("/system")
public class SystemController extends BaseController {

	@Autowired
	ApplicationConfig applicationConfig;
	
	@Autowired
	ApiConfig apiConfig;
	
	/** ping接口 */
	@RequestMapping(value = "/ping", method = RequestMethod.GET)
    public Object ping(HttpServletRequest request) {
		Stopwatch watch = Stopwatch.createStarted();
		return this.responseSuccess(watch,"ping success @"+DateTimeUtil.getNowDateTime());
    }

	/** 查看系统配置接口 */
	@RequestMapping(value = "/config", method = RequestMethod.GET)
	public Object config(HttpServletRequest request) {
		Stopwatch watch = Stopwatch.createStarted();
		Map<String, String> data = new LinkedHashMap<String, String>();
		data.put("env", applicationConfig.getEnv());
		data.put("api_config_update_interval_s", ""+applicationConfig.getApiConfigUpdateIntervalS());
		data.put("apiSize",""+apiConfig.size());
		return this.responseSuccess(watch, data);
	}
	
	/** 查看API接口映射 */
	@RequestMapping(value = "/getApiMapping", method = RequestMethod.GET)
	public Object getApiMapping(HttpServletRequest request) {
		Object res = null;
		Stopwatch watch = Stopwatch.createStarted();
		try {
			// 生产环境过滤
			String env = applicationConfig.getEnv();
			if (env.equals("prod")) {
				throw new Exception("this api is forbidden in prod env");
			}
			// 获取api配置
			String path = getNCParameter(request, "path");
			Api api = apiConfig.get(path);
			if (api==null) {
				throw new Exception("unknow api["+path+"]");
			}
			return this.responseSuccess(watch, api);
		} catch (Exception e) {
			res = responseError(watch, 100500, e.getMessage());
		}
        return res;
	}

	/** 全量更新API接口配置 */
	@RequestMapping(value = "/updateApiConfig", method = RequestMethod.GET)
	public Object updateApiConfig(HttpServletRequest request) {
		Stopwatch watch = Stopwatch.createStarted();
		try {
			// 生产环境过滤
			String env = applicationConfig.getEnv();
			if (env.equals("prod")) {
				throw new Exception("this api is forbidden in prod env");
			}
			apiConfig.loadAllFromDB();
			String res = "load_success "+apiConfig;
			return this.responseSuccess(watch, res);
		} catch (Exception e) {
			return responseError(watch, 100500, e.getMessage());
		}
	}
	
	
}

