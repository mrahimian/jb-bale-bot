package ir.jibit.directdebit.gateway.balejbbot.service;

import ir.jibit.directdebit.gateway.balejbbot.service.handlers.Handler;
import ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Role;

public interface AdminHandler<T, R> extends Handler<T, R> {
    boolean isAllowed(Role role);
}
