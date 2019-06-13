package org.gateway.service.auth;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

/**
 * Ticket登录鉴权
 * @author mawentao
 */
@Service
public class TicketAuth implements IAuth {

	@Override
	public void check(HttpServletRequest request) throws Exception {
		//1. 获取ticket
		String ticket = request.getHeader("X-TICKET");
		if (ticket==null) {
			throw new Exception("please login");
		}
		//2. 根据ticket解析uid
		String uid = ticket;
		request.setAttribute("X-GW-UID", uid);
	}
	
}
