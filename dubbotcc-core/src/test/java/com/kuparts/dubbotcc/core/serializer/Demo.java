package com.kuparts.dubbotcc.core.serializer;

import java.io.Serializable;

/**
 * project：dubbotcc-parent /www.kuparts.com
 * Created By chenbin on 2016/8/9 14:26
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class Demo implements Serializable{
    private String id;
    private String name;
    private String user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
