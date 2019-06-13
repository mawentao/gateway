package org.gateway.pojo;

/**
 * 返回包数据结构
 * @author admin
 * @since 2019-06-11 15:49
 *
 */

public class Response {
	/**
	 * 错误码
	 */
    private int errno;
    /**
     * 错误信息
     */
    private String errmsg;
    /**
     * 返回数据
     */
    private Object data;
    /**
     * 服务器处理时间
     */
    private String processTime;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getProcessTime() {
		return processTime;
	}

	public void setProcessTime(String processTime) {
		this.processTime = processTime;
	}

	@Override
    public String toString() {
        return "Response [errno=" + errno + ", errmsg=" + errmsg + ", processTime=" + processTime + ", data=" + data + "]";
    }
}
