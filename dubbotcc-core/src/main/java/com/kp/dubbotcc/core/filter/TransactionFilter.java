package com.kp.dubbotcc.core.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcInvocation;
import com.kp.dubbotcc.core.api.TccConstants;
import com.kp.dubbotcc.core.api.TccInvocation;
import com.kp.dubbotcc.core.api.TccServicePoint;
import com.kp.dubbotcc.core.api.Transaction;
import com.kp.dubbotcc.core.major.TccServicePointTrace;
import com.kp.dubbotcc.core.major.TransactionManager;
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
     * 补偿服务跟踪操作
     */
    private final TccServicePointTrace trace = new TccServicePointTrace();

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        /**
         * 获取相应参数
         */
        String methodName = invocation.getMethodName();
        Class serviceType = invoker.getInterface();
        Class[] args = invocation.getParameterTypes();
        /**
         * 获取补偿方法
         */
        String rollbackMethod = trace.getService().checkTccRollback(serviceType, methodName, args);//获取补偿方法
        /**
         * 判断是否存在补偿方法,如果不存在不实行补偿事务
         */
        Result result = null;
        if (!StringUtils.isBlank(rollbackMethod)) {
            /**
             * RPC上下文对象
             */
            RpcContext context = RpcContext.getContext();
            /**
             * 判断调用类型是属于调用方,还是提供方
             */
            if (context.isConsumerSide()) {
                return consumeSide(invoker, invocation, rollbackMethod);
                /**
                 * 如果是提供者
                 */
            } else if (context.isProviderSide()) {
                return providerSide(invoker, invocation, rollbackMethod);
            }
        } else {
            result = invoker.invoke(invocation);
        }
        return result;
    }

    /**
     * 服务提供方法事务管理实现
     *
     * @param invoker
     * @param invocation
     * @param rollbackMethod
     * @return
     */
    private Result providerSide(Invoker<?> invoker, Invocation invocation, String rollbackMethod) {
        return invoker.invoke(invocation);
    }

    /**
     * 服务调用方法事务管理实现
     *
     * @param invoker
     * @param invocation
     * @param rollbackMethod
     * @return
     */
    private Result consumeSide(Invoker<?> invoker, Invocation invocation, String rollbackMethod) {
        Transaction transaction;
        String methodName = invocation.getMethodName();
        Class serviceType = invoker.getInterface();
        Class[] args = invocation.getParameterTypes();
        /**
         * 构建节点信息
         */
        String interfaceName = invoker.getInterface().getName();
        String address = RpcContext.getContext().getLocalAddressString();
        int port = RpcContext.getContext().getLocalPort();
        Object[] Arguments = invocation.getArguments();
        //封装调用节点
        TccInvocation commit = new TccInvocation(serviceType, methodName, Arguments, args);
        TccInvocation rollback = new TccInvocation(serviceType, rollbackMethod, Arguments, args);
        TccServicePoint point; /**
         * 获取事务节点
         */
        transaction = TransactionManager.INSTANCE.getTransaction();
        /**
         * 如果本地不存在线程已执行节点
         */
        if (transaction == null) {
            transaction = TransactionManager.INSTANCE.begin();//开始事务;
        } else {
            String transIdExist = transaction.getTransId();
            transaction = TransactionManager.INSTANCE.getTransactionService().getTransactionByTransId(transIdExist);
        }
        point = trace.getService().generatePoint(methodName, transaction, interfaceName, address, port, commit, rollback);
        transaction.addServicePotin(point);
        //提交事务
        commit(transaction, invocation);
        //执行业务
        Result result = invoker.invoke(invocation);
        //事务回滚
        rollbackCheck(transaction, result, invocation);
        return result;
    }

    /**
     * 开如事务回滚并调用rollback方法
     *
     * @param transaction 事务信息
     * @param result      远程调用结果
     * @param invocation  执行器
     */
    private void rollbackCheck(Transaction transaction, Result result, Invocation invocation) {

    }

    /**
     * 事务提交前的操作
     */
    private void commit(Transaction transaction, Invocation invocation) {
        //提交事务
        RpcInvocation invocation1 = (RpcInvocation) invocation;
        invocation1.setAttachment(TccConstants.TRANS_ID, transaction.getTransId());
        TransactionManager.INSTANCE.commit(transaction);
    }
}
