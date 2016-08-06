package com.kuparts.dubbotcc.core.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcInvocation;
import com.kuparts.dubbotcc.api.TccConstants;
import com.kuparts.dubbotcc.api.TccInvocation;
import com.kuparts.dubbotcc.api.TccServicePoint;
import com.kuparts.dubbotcc.api.Transaction;
import com.kuparts.dubbotcc.commons.exception.TccRuntimeException;
import com.kuparts.dubbotcc.commons.utils.Assert;
import com.kuparts.dubbotcc.core.major.TransactionManager;
import com.kuparts.dubbotcc.core.service.TccServicePointService;
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
     * 打印日志
     */
    private static final Logger LOG = LoggerFactory.getLogger(TransactionFilter.class);
    /**
     * 补偿服务跟踪操作
     */
    private final TccServicePointService trace = new TccServicePointService();

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        invoker.getUrl().getIp();
        /**
         * 获取相应参数
         */
        String methodName = invocation.getMethodName();
        Class serviceType = invoker.getInterface();
        Class[] args = invocation.getParameterTypes();
        /**
         * 获取补偿方法
         */
        String rollbackMethod = trace.checkTccRollback(serviceType, methodName, args);//获取补偿方法
        /**
         * 判断是否存在补偿方法,如果不存在不实行补偿事务
         */
        Result result = null;
        if (StringUtils.isBlank(rollbackMethod)) {
            try {
                result = invoker.invoke(invocation);
            } catch (RuntimeException e) {
                if (StringUtils.isBlank(rollbackMethod)) {//当前调用的方法没有补偿方法
                    TransactionManager.INSTANCE.rollback();
                }
            }

        } else {
            //开始调用
            return tryCall(invoker, invocation, rollbackMethod);
        }
        return result;
    }

    /**
     * 服务调用方法事务管理实现
     *
     */
    private Result tryCall(Invoker<?> invoker, Invocation invocation, String rollbackMethod) {
        Transaction transaction = null;
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
        /**
         * 判断调用类型是属于调用方,还是提供方
         */
        RpcContext context = RpcContext.getContext();
        if (context.isConsumerSide()) {
            /** 获取事务节点
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
        } else if (context.isProviderSide()) {

            String transIdExist = invocation.getAttachment(TccConstants.TRANS_ID);
            transaction = TransactionManager.INSTANCE.getTransactionService().getTransactionByTransId(transIdExist);
        }
        TccServicePoint point = trace.generatePoint(transaction, interfaceName, address, port, invoker.getUrl(), commit, rollback);
        Result result = null;
        try {
            //如果发现获取的事务为空,进行回滚处理
            Assert.notNull(transaction);//transaction不为空
            transaction.addServicePotin(point);
            //提交事务
            commit(transaction, invocation);
            //执行业务
            result = invoker.invoke(invocation);
            //事务回滚
            rollbackCheck(transaction, result, invocation);

        } catch (RuntimeException ex) {//有运行时异常,则进行事务回滚
            TransactionManager.INSTANCE.rollback();
        }
        return result;
    }

    /**
     * 检查是否需要回滚事务
     *
     * @param transaction 事务信息
     * @param result      远程调用结果
     * @param invocation  执行器
     */
    private void rollbackCheck(Transaction transaction, Result result, Invocation invocation) {
        /**
         * 如果执行的方法发生了异常,则需要进行回滚
         */
        if (result != null) {
            if (result.hasException() && RuntimeException.class.isAssignableFrom(result.getException().getClass())) {
                throw new TccRuntimeException(result.getException().getMessage());
            }
        }
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
