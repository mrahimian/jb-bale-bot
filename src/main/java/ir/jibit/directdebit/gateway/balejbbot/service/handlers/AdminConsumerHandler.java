package ir.jibit.directdebit.gateway.balejbbot.service.handlers;

import ir.jibit.directdebit.gateway.balejbbot.service.handlers.Allowable;

import java.util.function.Consumer;

public interface AdminConsumerHandler<T> extends Consumer<T>, Allowable {
}
