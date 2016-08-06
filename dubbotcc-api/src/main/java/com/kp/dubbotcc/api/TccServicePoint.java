package com.kp.dubbotcc.api;

import com.kp.dubbotcc.commons.emuns.ServicePointStatus;
import com.kp.dubbotcc.commons.utils.DateUtils;
import com.kp.dubbotcc.commons.utils.GenerateUniqueId;

import java.io.Serializable;

/**
 * 服务调用的接口点
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class TccServicePoint implements Serializable {
    private static final long serialVersionUID = 8796306822570541845L;
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
     * 本地服务地址
     */
    private String address;
    /**
     * 端口
     */
    private Integer port;
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
    /**
     * 分组
     */
    private String group;
    /**
     * 版本号
     */
    private String version;
    /**
     * 远程服务IP
     */
    private String remoteAddress;

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public String getGroup() {
        return group;
    }

    public String getVersion() {
        return version;
    }

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

    public String getAddress() {
        return address;
    }

    public Integer getPort() {
        return port;
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

    private TccServicePoint setValue(ServicePointBuilder builder) {
        transId = builder.transId;
        parentId = builder.parentId;
        commitInvocation = builder.commitInvocation;
        pointId = builder.pointId;
        isRoot = builder.isRoot;
        rollbackInvocation = builder.rollbackInvocation;
        serviceName = builder.serviceName;
        startTime = builder.startTime;
        address = builder.address;
        port = builder.port;
        status = builder.status;
        version = builder.version;
        group = builder.group;
        remoteAddress = builder.remoteAddress;
        return this;
    }

    /**
     * 修改执行服务点的状态
     *
     * @param status 状态
     */
    public void modifyStatus(ServicePointStatus status) {
        this.status = status;
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
         * 本地服务地址
         */
        private String address;
        /**
         * 远程服务IP
         */
        private String remoteAddress;
        /**
         * 端口
         */
        private Integer port;
        /**
         * 事务提交器
         */
        private TccInvocation commitInvocation;
        /**
         * 事务补偿器
         */
        private TccInvocation rollbackInvocation;
        /**
         * 版本号
         */
        private String version;
        /**
         * 是否为根事务
         */
        private boolean isRoot;
        /**
         * 分组
         */
        public String group;

        public ServicePointBuilder setVersion(String version) {
            this.version = version;
            return this;
        }

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

        public ServicePointBuilder setGroup(String group) {
            this.group = group;
            return this;
        }

        public ServicePointBuilder setAddress(String address) {
            this.address = address;
            return this;
        }

        public ServicePointBuilder setPort(Integer port) {
            this.port = port;
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

        public ServicePointBuilder setParentId(String parentId) {
            this.parentId = parentId;
            return this;
        }

        public ServicePointBuilder setRemoteAddress(String remoteAddress) {
            this.remoteAddress = remoteAddress;
            return this;
        }

        /**
         * 构建servicePoint
         */
        public TccServicePoint build() {
            startTime = DateUtils.nowEpochSecond();
            pointId = GenerateUniqueId.getInstance().getUniqIDHashString();
            return new TccServicePoint().setValue(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TccServicePoint that = (TccServicePoint) o;

        return pointId.equals(that.pointId);

    }

    @Override
    public int hashCode() {
        return pointId.hashCode();
    }
}
