package org.gateway.service;

import javax.servlet.http.HttpServletRequest;

import org.gateway.ApplicationBootListener;
import org.gateway.service.auth.TicketAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 鉴权服务
 * @author mawentao
 *
 */
@Service
public class AuthService {
	
	private static final Logger logger = LoggerFactory.getLogger(ApplicationBootListener.class);
	
	@Autowired
	TicketAuth ticketAuth;
	
	/**
	 * 根据authKey鉴权
	 * @param authKey
	 * @param request
	 * @return
	 * @throws Exception （鉴权失败抛异常）
	 */
	public void check(String authKey,HttpServletRequest request) throws Exception {
		logger.debug("AuthService::check("+authKey+")");
		if (authKey=="ticket") {
			ticketAuth.check(request); 
		} else {
			logger.warn("unknow auth key:"+authKey);
		}
		return;
	}
}
