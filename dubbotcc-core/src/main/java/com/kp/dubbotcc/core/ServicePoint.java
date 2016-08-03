package com.kp.dubbotcc.core;

import com.kp.dubbotcc.commons.emuns.ServicePointStatus;
import com.kp.dubbotcc.commons.utils.DateUtils;
import com.kp.dubbotcc.commons.utils.GenerateUniqueId;

/**
 * 服务调用的接口点
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class ServicePoint {
    /**
     * 事务唯一ID
     */
    private String transId;
    /**
     * 服务点ID
     */
    private String pointId;
    /**
     * 父节点服务ID
     */
    private String parentId;
    /**
     * 执行状态
     */
    private ServicePointStatus status;
    /**
     * 开始时间
     */
    private Long startTime;
    /**
     * 服务接口名称
     */
    private String serviceName;
    /**
     * 调用的方法名
     */
    private String callMethod;
    /**
     * dubbo服务的调用url
     */
    private String serviceUrl;
    /**
     * 事务提交器
     */
    private TccInvocation commitInvocation;
    /**
     * 事务补偿器
     */
    private TccInvocation rollbackInvocation;
    /**
     * 是否为根事务
     */
    private boolean isRoot;

    public String getTransId() {
        return transId;
    }

    public String getPointId() {
        return pointId;
    }

    public String getParentId() {
        return parentId;
    }

    public ServicePointStatus getStatus() {
        return status;
    }

    public Long getStartTime() {
        return startTime;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getCallMethod() {
        return callMethod;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public TccInvocation getCommitInvocation() {
        return commitInvocation;
    }

    public TccInvocation getRollbackInvocation() {
        return rollbackInvocation;
    }

    public boolean isRoot() {
        return isRoot;
    }

    private ServicePoint setValue(ServicePointBuilder builder) {
        this.transId = builder.transId;
        this.parentId = builder.parentId;
        this.callMethod = builder.callMethod;
        this.commitInvocation = builder.commitInvocation;
        this.callMethod = builder.callMethod;
        this.pointId = builder.pointId;
        this.isRoot = builder.isRoot;
        this.rollbackInvocation = builder.rollbackInvocation;
        this.serviceName = builder.serviceName;
        this.serviceUrl = builder.serviceUrl;
        this.startTime = builder.startTime;
        this.status = builder.status;
        return this;
    }

    /**
     * 服务构建器
     */
    public static class ServicePointBuilder {
        /**
         * 事务唯一ID
         */
        private String transId;
        /**
         * 服务点ID
         */
        private String pointId;
        /**
         * 父节点服务ID
         */
        private String parentId;
        /**
         * 执行状态
         */
        private ServicePointStatus status;
        /**
         * 开始时间
         */
        private Long startTime;
        /**
         * 服务接口名称
         */
        private String serviceName;
        /**
         * 调用的方法名
         */
        private String callMethod;
        /**
         * dubbo服务的调用url
         */
        private String serviceUrl;
        /**
         * 事务提交器
         */
        private TccInvocation commitInvocation;
        /**
         * 事务补偿器
         */
        private TccInvocation rollbackInvocation;
        /**
         * 是否为根事务
         */
        private boolean isRoot;

        public ServicePointBuilder setTransId(String transId) {
            this.transId = transId;
            return this;
        }

        public ServicePointBuilder setStatus(ServicePointStatus status) {
            this.status = status;
            return this;
        }

        public ServicePointBuilder setServiceName(String serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        public ServicePointBuilder setCallMethod(String callMethod) {
            this.callMethod = callMethod;
            return this;
        }

        public ServicePointBuilder setServiceUrl(String serviceUrl) {
            this.serviceUrl = serviceUrl;
            return this;
        }

        public ServicePointBuilder setCommitInvocation(TccInvocation commitInvocation) {
            this.commitInvocation = commitInvocation;
            return this;
        }

        public ServicePointBuilder setRollbackInvocation(TccInvocation rollbackInvocation) {
            this.rollbackInvocation = rollbackInvocation;
            return this;
        }

        public ServicePointBuilder setRoot(boolean root) {
            isRoot = root;
            return this;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        /**
         * 构建servicePoint
         */
        public ServicePoint build() {
            this.startTime = DateUtils.nowEpochSecond();
            this.pointId = GenerateUniqueId.getInstance().getUniqID();
            if (this.isRoot) {
                this.parentId = "";
            }
            return new ServicePoint().setValue(this);
        }
    }
}
