package org.gateway.service;

import javax.servlet.http.HttpServletRequest;

/**
 * 代理转发接口
 * @author mawentao
 */
public interface IForward {
	
	/**
	 * 代理转发
	 * @param request
	 * @return
	 */
	public Object forward(HttpServletRequest request, String url) throws Exception;
	
}
