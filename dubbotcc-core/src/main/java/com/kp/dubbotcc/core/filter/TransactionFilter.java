package com.kp.dubbotcc.core.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.kp.dubbotcc.core.ServicePoint;
import com.kp.dubbotcc.core.TccInvocation;
import com.kp.dubbotcc.core.Transaction;
import com.kp.dubbotcc.core.service.TccServicePointTrace;
import com.kp.dubbotcc.core.service.TransactionManager;
import org.apache.commons.lang3.StringUtils;

/**
 * 拦截dubbo调用,进行事务过滤调用
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
@Activate(group = {Constants.SERVER_KEY, Constants.CONSUMER})
public class TransactionFilter implements Filter {
    /**
     * 补偿服务实现
     */
    private TccServicePointTrace trace = null;

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Result result = null;
        /**
         * 获取相应参数
         */
        String methodName = invocation.getMethodName();
        Class serviceType = invoker.getInterface();
        Class[] args = invocation.getParameterTypes();
        /**
         * 获取补偿方法
         */
        trace = new TccServicePointTrace();
        String rollbackMethod = trace.getService().checkTccRollback(serviceType, methodName, args);//获取补偿方法
        /**
         * 判断是否存在补偿方法,如果不存在不实行补偿事务
         */
        if (!StringUtils.isBlank(rollbackMethod)) {
            Transaction transaction = TransactionManager.INSTANCE.begin();//开始事务
            /**
             * 构建节点信息
             */
            String interfaceName = invoker.getInterface().getName();
            String url = invoker.getUrl().toFullString();
            Object[] Arguments = invocation.getArguments();
            //封装调用节点
            TccInvocation commit = new TccInvocation(serviceType, methodName, Arguments, args);
            TccInvocation rollback = new TccInvocation(serviceType, rollbackMethod, Arguments, args);
            /**
             * RPC上下文对象
             */
            RpcContext context = RpcContext.getContext();
            ServicePoint point = null;//生成当前的调用point
            /**
             * 判断调用类型是属于调用方,还是提供方
             */
            if (context.isConsumerSide()) {
                /**
                 * 获取事务节点
                 */
                ServicePoint localPoint = TransactionManager.INSTANCE.getServicePoint();
                /**
                 * 如果本地不存在线程已执行节点
                 */
                if (null == localPoint) {
                    point = trace.getService().generatePoint(methodName, transaction, interfaceName, url, commit, rollback);
                } else {
                    String transIdExist = localPoint.getTransId();
                    transaction = TransactionManager.INSTANCE.getTransactionService().getServicePointByTransId(transIdExist);
                    point = trace.getService().generatePoint(methodName, transaction, interfaceName, url, commit, rollback);
                }
            } else if (context.isProviderSide()) {

            }
            //提交事务
            trace.setPoint(point);
            TransactionManager.INSTANCE.commit(trace);
        } else {
            result = invoker.invoke(invocation);
        }
        return result;
    }
}
