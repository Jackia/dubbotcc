package com.kuparts.dubbotcc.core.dispatch.propety;

import java.io.Serializable;
import java.util.List;

/**
 * 网络传输之间命令
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class InvokeCommand implements Serializable {
    private static final long serialVersionUID = -8215894982998755463L;
    //执行者序号
    private long number;
    //需要接管的目标
    private List<Actor> targetActor;
    //序列化ID
    private String sid;
    //命令类型
    private int commandType;

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public int getCommandType() {
        return commandType;
    }

    public void setCommandType(int commandType) {
        this.commandType = commandType;
    }

    public List<Actor> getTargetActor() {
        return targetActor;
    }

    public void setTargetActor(List<Actor> targetActor) {
        this.targetActor = targetActor;
    }

    public InvokeCommand(long number, String sid) {
        this.number = number;
        this.sid = sid;
    }

    public InvokeCommand() {
    }

    @Override
    public String toString() {
        return "InvokeCommand{" +
                "number=" + number +
                ", sid='" + sid + '\'' +
                ", commandType=" + commandType +
                '}';
    }
}
