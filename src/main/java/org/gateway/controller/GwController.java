package org.gateway.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.gateway.config.ApiConfig;
import org.gateway.config.BaseController;
import org.gateway.pojo.Api;
import org.gateway.service.AuthService;
import org.gateway.service.HttpForward;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Stopwatch;

@Controller
public class GwController extends BaseController {
	
	@Autowired
	ApiConfig apiConfig;
	
	@Autowired
	HttpForward httpForward;
	
	@Autowired
	AuthService authService;
	
	@RequestMapping("/**")
    @ResponseBody
    public Object gwdo(HttpServletRequest request) {
		Object res = null;
		try {
			//1. 获取URI
			String uri = request.getRequestURI();
			Api api = apiConfig.get(uri);
			if (api==null) {
				throw new Exception("unknow api:"+uri);
			}
			//2. 鉴权处理
			int authCnt = api.getAuthKeys().size();
			if (authCnt>0) {
				List<String> authKeys = api.getAuthKeys();
				for (int i=0;i<authCnt; ++i) {
					String authKey = authKeys.get(i);
					authService.check(authKey, request);
				}
			}
			//3. Mock
			if (api.isMock()) {
				return api.getMockData();
			}
			//4. HTTP代理转发
			logger.debug(api.getApi()+"->"+api.getUri());
			res = httpForward.forward(request,api);
		} catch (Exception e) {
			Stopwatch stopwatch = Stopwatch.createStarted();
			res = responseError(stopwatch,100500, e.getMessage());
		}
        return res;
    }
}
