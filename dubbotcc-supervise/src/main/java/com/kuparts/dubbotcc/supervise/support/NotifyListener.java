package com.kuparts.dubbotcc.supervise.support;

import com.kuparts.dubbotcc.supervise.propety.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 命令通知
 *
 * @author chenbin@kuparts.com
 * @author chenbin
 * @version 1.0
 **/
public abstract class NotifyListener implements Notify {
    /**
     * 通知节点变化,同步通知
     *
     * @param type    管理节点类型
     * @param childs  变化的节点
     * @param command 命令
     */
    public boolean notify(final ActorType type, final InvokeCommand command, final List<Actor> childs, NotifyCallback... callbacks) {
        try {
            CommandType commandType = getCommandType(type, childs);
            if (commandType == CommandType.NONE) {
                return true;
            }
            Actor actor = getTargetActor(ActorType.SLAVE);
            if (actor == null) {
                return false;
            }
            command.setNumber(actor.getNumber());
            command.setCommandType(commandType.ordinal());
            return execut(actor, command, callbacks);
        } catch (RuntimeException ex) {
            return false;
        }
    }

    /**
     * 通知节点变化,异步通知
     *
     * @param type       变化的节点
     * @param targetType 目标节点
     * @param command    命令
     */
    public void asyncNotify(final ActorType type, final InvokeCommand command, final List<Actor> childs, NotifyCallback... callbacks) {
        CompletableFuture.supplyAsync(new BooleanSupplier(type, command, childs)).thenAccept(flag -> {
            if (flag) {//通知成功
                System.out.println("通知成功.....");
            } else {//通知失败
                System.out.println("通知失败.....");
            }
        });
    }

    /**
     * 根据不同的类型获到不同的节点
     *
     * @param type type
     * @return 需要通知的节点
     */
    private Actor getTargetActor(ActorType type) {
        List<Actor> result = Context.getContext().getActors().stream()
                .filter(e -> e.getValue().getType() == type).map(e -> e.getValue())
                .collect(Collectors.toList());
        if (result != null && !result.isEmpty()) {
            Random r = new Random();
            int n = r.nextInt(result.size());
            return result.get(n);
        }
        return null;
    }

    /**
     * 获取命令类型
     *
     * @param childs 根据变化节点
     * @return 命令类型
     */
    private CommandType getCommandType(final ActorType type, List<Actor> childs) {
        long count;
        switch (type) {
            case MASTER:
                count = childs.stream().filter(e -> e.getType() == ActorType.OBSERVE).count();
                if (count > 0) {
                    return CommandType.SLAVE_OBSERVE;
                }
                count = childs.stream().filter(e -> e.getType() == ActorType.SLAVE).count();
                if (count > 0) {
                    return CommandType.SLAVE_EX;
                }
                break;
            case OBSERVE:
                count = childs.stream().filter(e -> e.getType() == ActorType.MASTER).count();
                if (count > 0) {
                    return CommandType.SLAVE_MASTER;
                }
                break;
            case SLAVE:
                count = childs.stream().filter(e -> e.getType() == ActorType.MASTER).count();
                if (count > 0) {
                    return CommandType.SLAVE_MASTER_SLAVE;
                }
                break;
        }
        return CommandType.NONE;
    }

    protected abstract boolean execut(Actor targetAcotr, InvokeCommand command, NotifyCallback... callbacks);

    private class BooleanSupplier implements Supplier<Boolean> {

        private final ActorType type;
        private final InvokeCommand command;
        private final List<Actor> actors;
        private final NotifyCallback[] callbacks;

        public BooleanSupplier(ActorType type, InvokeCommand command, final List<Actor> childs, NotifyCallback... callbacks) {
            this.type = type;
            this.command = command;
            this.actors = childs;
            this.callbacks = callbacks;
        }

        @Override
        public Boolean get() {
            return NotifyListener.this.notify(type, command, actors, callbacks);
        }
    }
}
