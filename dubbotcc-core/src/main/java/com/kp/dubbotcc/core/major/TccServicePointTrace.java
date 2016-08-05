package com.kp.dubbotcc.core.major;

import com.kp.dubbotcc.core.api.TccServicePoint;
import com.kp.dubbotcc.core.service.TccServicePointService;

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

    private TccServicePoint point;

    public TccServicePointTrace() {
    }

    /**
     * 设置一个point
     *
     * @param point
     */
    public TccServicePointTrace(TccServicePoint point) {
        this.point = point;
    }

    /**
     * 获取设置的point
     *
     * @return
     */
    public TccServicePoint getPoint() {
        return point;
    }

    /**
     * 设置一个point
     *
     * @param point
     */
    public void setPoint(TccServicePoint point) {
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
