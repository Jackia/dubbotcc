package com.kuparts.dubbotcc.core.dispatch.propety;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 命令类型
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public enum CommandType {
    //提升权限SLAVE到OBSERVE--->MASTER管理
    //提升权限SLAVE到MASTER---->OBSERVE管理
    //接管事务SLAVE节点执行---->MASTER管理
    //提升权限SLAVE到MASTER---->SLAVE管理
    SLAVE_OBSERVE,
    SLAVE_MASTER,
    SLAVE_EX,
    SLAVE_MASTER_SLAVE,
    NONE;

    public static CommandType parse(int i) {
        switch (i) {
            case 0:
                return SLAVE_OBSERVE;
            case 1:
                return SLAVE_MASTER;
            case 2:
                return SLAVE_EX;
            case 3:
                return SLAVE_MASTER_SLAVE;
            case 4:
                return NONE;
        }
        return null;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface CommandTask {
        CommandType value();
    }
}
