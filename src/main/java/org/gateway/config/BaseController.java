package org.gateway.config;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import com.google.common.base.Stopwatch;
import org.gateway.pojo.Response;

/**
 * 所有Controller的抽象父类
 * @author admin
 * @since 2019-06-11 15:49
 *
 */
public abstract class BaseController {

	/**
	 * 日志
	 */
	protected Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * 获取必填请求参数
	 */
	final protected String getNCParameter(HttpServletRequest request, String name) throws Exception {
		String res = request.getParameter(name);
		if (res==null) {
			throw new Exception("undefined parameter:"+name);
		}
		return res;
	}
	
	/**
	 * 正常输出
     * @param stopwatch
	 * @param data
	 * @return
	 */
	final protected Object responseSuccess(Stopwatch stopwatch,Object data) {
		Response response = new Response();
		response.setErrno(0);
		response.setErrmsg("success");
		response.setData(data);
		stopwatch.stop();
		response.setProcessTime(stopwatch.toString());
		this.logger.info(response);
		return response;
	}
	
	/**
	 * 异常输出
     * @param stopwatch
	 * @param errno
	 * @param errmsg
	 * @return
	 */
	final protected Object responseError(Stopwatch stopwatch,int errno,String errmsg) {
		Response response = new Response();
		response.setErrno(errno);
		response.setErrmsg(errmsg);
		response.setData(null);
        stopwatch.stop();
        response.setProcessTime(stopwatch.toString());
		this.logger.info(response);
		return response;
	}
	
}
