package com.kuparts.dubbotcc.core.service;

import com.alibaba.dubbo.common.URL;
import com.kuparts.dubbotcc.api.Compensation;
import com.kuparts.dubbotcc.commons.api.TccConstants;
import com.kuparts.dubbotcc.commons.api.TccInvocation;
import com.kuparts.dubbotcc.commons.api.TccServicePoint;
import com.kuparts.dubbotcc.commons.api.Transaction;
import com.kuparts.dubbotcc.commons.emuns.ServicePointStatus;
import com.kuparts.dubbotcc.commons.exception.TccRuntimeException;
import com.kuparts.dubbotcc.commons.utils.Assert;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;

/**
 * 补偿事务执行器
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class TccServicePointService {
    /**
     * 查找是否有存在的事务执行器
     *
     * @param serviceType 接口
     * @param methodName  方法
     * @param args        方法参数
     * @return 事务执行器
     * @throws TccRuntimeException
     */
    public String checkTccRollback(Class serviceType, String methodName, Class... args) throws TccRuntimeException {
        try {
            Assert.notNull(serviceType);
            Assert.notNull(methodName);
            Assert.notNull(args);
            Method method = serviceType.getDeclaredMethod(methodName, args);
            Compensation rollback = method.getAnnotation(Compensation.class);
            if (rollback != null) {
                String value = rollback.value();
                if (StringUtils.isBlank(value)) {
                    return "";
                }
                return value;
            }
        } catch (NoSuchMethodException e) {
            throw new TccRuntimeException("not fount method " + e.getMessage());
        }
        return "";
    }
    /**
     * 生成服务点
     *
     * @param transaction   事务
     * @param interfaceName 接口名
     * @param address       本地服务地址
     * @param port          本地服务端口
     * @param commit        提交者
     * @param rollback      补偿者
     * @param url           地址
     * @return 服务点
     */
    public TccServicePoint generatePoint(Transaction transaction,
                                         String interfaceName,
                                         String address,
                                         int port,
                                         URL url,
                                         TccInvocation commit,
                                         TccInvocation rollback) {
        //是否为你节点
        boolean isRoot = false;
        String parentId = "";
        if (transaction.getPotins().isEmpty()) {
            isRoot = true;
        } else {
            parentId = lastPoint(transaction).getParentId();
        }
        String version = url.getParameter(TccConstants.VERSION);
        String group = url.getParameter(TccConstants.GROUP);
        return new TccServicePoint.ServicePointBuilder()
                .setGroup(group)
                .setCommitInvocation(commit)
                .setRollbackInvocation(rollback)
                .setServiceName(interfaceName)
                .setAddress(address)
                .setPort(port)
                .setStatus(ServicePointStatus.INIT)
                .setTransId(transaction.getTransId())
                .setRoot(isRoot)
                .setVersion(version)
                .setRemoteAddress(url.getAddress())
                .setParentId(parentId).build();
    }

    /**
     * 获取当前事务的最后一个节点
     *
     * @param transaction 事务对象
     * @return 最后一个节点
     */
    public TccServicePoint lastPoint(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        if (transaction.getPotins() == null || transaction.getPotins().size() <= 0) {
            return null;
        }
        int size = transaction.getPotins().size();
        return transaction.getPotins().get(size - 1);
    }

    /**
     * 修改事务的当前节点的状态信息
     *
     * @param transaction 当前事务
     * @param status      修改的状态
     * @return 修改后的事务
     */
    public Transaction modifyCurrentStatus(Transaction transaction, ServicePointStatus status) {
        TccServicePoint point = lastPoint(transaction);
        if (point != null) {
            point.modifyStatus(status);
        }
        return transaction;
    }
}
