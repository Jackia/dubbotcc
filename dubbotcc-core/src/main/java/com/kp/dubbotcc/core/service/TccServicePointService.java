package com.kp.dubbotcc.core.service;

import com.kp.dubbotcc.commons.emuns.ServicePointStatus;
import com.kp.dubbotcc.commons.exception.TccRuntimeException;
import com.kp.dubbotcc.commons.utils.Assert;
import com.kp.dubbotcc.core.api.Compensation;
import com.kp.dubbotcc.core.api.TccInvocation;
import com.kp.dubbotcc.core.api.TccServicePoint;
import com.kp.dubbotcc.core.api.Transaction;
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
    public String checkTccRollback(Class serviceType, String methodName, Class[] args) throws TccRuntimeException {
        try {
            Assert.notNull(serviceType);
            Assert.notNull(methodName);
            Assert.notNull(args);
            Method method = serviceType.getDeclaredMethod(methodName, args);
            Compensation rollback = method.getAnnotation(Compensation.class);
            if (rollback != null) {
                String value = rollback.value();
                if (StringUtils.isBlank(value)) {
                    return "tcc" + methodName;
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
     * @param methodName    方法名
     * @param transaction   事务
     * @param interfaceName 接口名
     * @param address       本地服务地址
     * @param port          本地服务端口
     * @param commit        提交者
     * @param rollback      补偿者
     * @return 服务点
     */
    public TccServicePoint generatePoint(String methodName,
                                         Transaction transaction,
                                         String interfaceName,
                                         String address,
                                         int port,
                                         TccInvocation commit,
                                         TccInvocation rollback) {
        //是否为你节点
        boolean isRoot = false;
        String parentId = "";
        if (transaction.getPotins().isEmpty()) {
            isRoot = true;
        } else {
            int size = transaction.getPotins().size();
            parentId = transaction.getPotins().get(size - 1).getPointId();
        }
        return new TccServicePoint.ServicePointBuilder()
                .setCallMethod(methodName)
                .setCommitInvocation(commit)
                .setRollbackInvocation(rollback)
                .setServiceName(interfaceName)
                .setAddress(address)
                .setPort(port)
                .setStatus(ServicePointStatus.INIT)
                .setTransId(transaction.getTransId())
                .setRoot(isRoot)
                .setParentId(parentId).build();
    }
}
