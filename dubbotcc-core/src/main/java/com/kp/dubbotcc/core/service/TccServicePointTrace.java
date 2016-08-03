package com.kp.dubbotcc.core.service;

import com.kp.dubbotcc.core.ServicePoint;

/**
 * 当前事务调用链
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class TccServicePointTrace {
    /**
     * 服务操作
     */
    private TccServicePointService service = new TccServicePointService();

    private ServicePoint point;

    public TccServicePointTrace() {
    }

    /**
     * 设置一个point
     *
     * @param point
     */
    public TccServicePointTrace(ServicePoint point) {
        this.point = point;
    }

    /**
     * 获取设置的point
     *
     * @return
     */
    public ServicePoint getPoint() {
        return point;
    }

    /**
     * 设置一个point
     *
     * @param point
     */
    public void setPoint(ServicePoint point) {
        this.point = point;
    }

    /**
     * 获取服务对象
     *
     * @return
     */
    public TccServicePointService getService() {
        return service;
    }

}
