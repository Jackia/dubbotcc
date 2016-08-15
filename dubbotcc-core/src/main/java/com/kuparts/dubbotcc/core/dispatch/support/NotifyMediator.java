package com.kuparts.dubbotcc.core.dispatch.support;

import com.kuparts.dubbotcc.commons.exception.TccRuntimeException;
import com.kuparts.dubbotcc.commons.utils.Assert;
import com.kuparts.dubbotcc.core.dispatch.propety.Actor;
import com.kuparts.dubbotcc.core.dispatch.propety.InvokeCommand;
import com.kuparts.dubbotcc.core.dispatch.propety.NotifyClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public class NotifyMediator implements Mediator {

    private final Map<String, Notify> clients = new ConcurrentHashMap<>();

    @Override
    public void execute(NotifyClient client, Actor actor, InvokeCommand command, NotifyCallback... callback) {
        Assert.notNull(client);
        Assert.notNull(actor);
        Assert.notNull(command);
        if (clients.containsKey(client.name())) {
            clients.get(client.name()).notify(actor, command, callback);
        } else {
            throw new TccRuntimeException("not fount notifyCline:" + client.name());
        }
    }
    @Override
    public void register(NotifyClient client, Notify notify) {
        if (client == null) {
            return;
        }
        if (notify == null) {
            return;
        }
        clients.put(client.name(), notify);
    }
}
