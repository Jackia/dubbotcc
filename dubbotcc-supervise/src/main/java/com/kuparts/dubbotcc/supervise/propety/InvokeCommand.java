package com.kuparts.dubbotcc.supervise.propety;

import java.io.Serializable;

/**
 * 网络传输之间命令
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class InvokeCommand implements Serializable {
    //执行者序号
    private int number;
    //序列化ID
    private String sid;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public InvokeCommand(int number, String sid) {
        this.number = number;
        this.sid = sid;
    }

    public InvokeCommand() {
    }
}
