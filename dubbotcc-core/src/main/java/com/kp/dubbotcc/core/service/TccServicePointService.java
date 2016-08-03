package com.kp.dubbotcc.core.service;

import com.kp.dubbotcc.commons.emuns.ServicePointStatus;
import com.kp.dubbotcc.commons.exception.TccRuntimeException;
import com.kp.dubbotcc.commons.utils.Assert;
import com.kp.dubbotcc.core.ServicePoint;
import com.kp.dubbotcc.core.TccInvocation;
import com.kp.dubbotcc.core.Transaction;
import com.kp.dubbotcc.core.api.TccRollback;
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
            TccRollback rollback = method.getAnnotation(TccRollback.class);
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
     * @param url           URL
     * @param commit        提交者
     * @param rollback      补偿者
     * @return 服务点
     */
    public ServicePoint generatePoint(String methodName, Transaction transaction, String interfaceName, String url, TccInvocation commit, TccInvocation rollback) {
        ServicePoint point;
        point = new ServicePoint.ServicePointBuilder()
                .setCallMethod(methodName)
                .setCommitInvocation(commit)
                .setRollbackInvocation(rollback)
                .setServiceName(interfaceName)
                .setServiceUrl(url)
                .setStatus(ServicePointStatus.INIT)
                .setTransId(transaction.getTransId())
                .setRoot(true).build();
        return point;
    }
}
