package org.gateway.filter;


import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.validation.Validator;

/**
 * 访问日志，每次请求记录日志
 * @author admin
 * @since 2019-06-11 15:49
 *
 */

@Order(0)
@WebFilter(filterName = "PvFilter", urlPatterns = "/*")
public class PvFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(PvFilter.class);
	
	@Autowired
	private Validator validator;
	
	public void destroy() {
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filterChain) throws IOException, ServletException {
		// 访问URL
		HttpServletRequest request = (HttpServletRequest)servletRequest;
		String url = request.getRequestURL().toString();
		String queryString = request.getQueryString();
		if (queryString!=null) {
			url += "?"+queryString;
		}
		// 客户端信息
		String from = request.getRemoteAddr()+":"+request.getRemotePort();
		String log = "[PV]\t"+url+"\tfrom:"+from+
				"||userAgent:"+request.getHeader("User-Agent");
		logger.info(log);
		filterChain.doFilter(servletRequest, servletResponse);
	}

	public void init(FilterConfig arg0) throws ServletException {
	}
}
