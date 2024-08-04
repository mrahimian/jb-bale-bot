package ir.jibit.directdebit.gateway.balejbbot.service.handlers;

import ir.jibit.directdebit.gateway.balejbbot.service.handlers.Allowable;

import java.util.function.Supplier;

public interface AdminSupplierHandler<T> extends Supplier<T>, Allowable {
}
