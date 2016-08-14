package com.kuparts.dubbotcc.supervise.propety;

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

    public CommandType parse(int i) {
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
}
