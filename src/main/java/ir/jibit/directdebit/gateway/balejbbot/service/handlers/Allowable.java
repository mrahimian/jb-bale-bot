package ir.jibit.directdebit.gateway.balejbbot.service.handlers;

import ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Role;

public interface Allowable {
    boolean isAllowed(Role role);
}
