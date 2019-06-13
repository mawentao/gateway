package org.gateway.service.auth;

import javax.servlet.http.HttpServletRequest;

/**
 * 鉴权接口
 * @author mawentao
 *
 */
public interface IAuth {
	
	/**
	 * 鉴权
	 * @param request
	 * @throws Exception
	 */
	public void check(HttpServletRequest request) throws Exception;

}
