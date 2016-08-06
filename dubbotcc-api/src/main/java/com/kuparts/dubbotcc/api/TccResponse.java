package com.kuparts.dubbotcc.api;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 回调信息返回
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class TccResponse implements Serializable {
    private static final long serialVersionUID = -6745628093904011838L;
    /**
     * 事务ID
     */
    private String transId;
    /**
     * 节点ID
     */
    private String pointId;
    /**
     * 失败节点ID
     */
    private String failureId;
    /**
     * 调用是否成功
     */
    private boolean successful;
    /**
     * 如果不成功,保存异常信息
     */
    private transient Throwable error;
    /**
     * 错误信息
     */
    private String errorMsg;
    /**
     * 成功返回的值是什么
     */
    private Object obj;
    /**
     * 回调的参数值
     */
    private Object[] args;
    /**
     * 回调的方法
     */
    private String rollbackMethod;
    /**
     * 调用的接口名称
     */
    private String interfaceName;
    /**
     * 远程地址
     */
    private String remoteAddress;
    /**
     * 端口号
     */
    private int port;

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public Throwable getError() {
        return error;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setError(Throwable error) {
        if (error != null) {
            errorMsg = error.getMessage();
        }
        this.error = error;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getRollbackMethod() {
        return rollbackMethod;
    }

    public void setRollbackMethod(String rollbackMethod) {
        this.rollbackMethod = rollbackMethod;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getPointId() {
        return pointId;
    }

    public void setPointId(String pointId) {
        this.pointId = pointId;
    }

    public String getFailureId() {
        return failureId;
    }

    public void setFailureId(String failureId) {
        this.failureId = failureId;
    }

    @Override
    public String toString() {
        return "TccResponse{" +
                "transId='" + transId + '\'' +
                ", pointId='" + pointId + '\'' +
                ", failureId='" + failureId + '\'' +
                ", successful=" + successful +
                ", errorMsg='" + errorMsg + '\'' +
                ", rollbackMethod='" + rollbackMethod + '\'' +
                ", interfaceName='" + interfaceName + '\'' +
                ", remoteAddress='" + remoteAddress + '\'' +
                ", port=" + port +
                '}';
    }
}
