package ir.jibit.directdebit.gateway.balejbbot.service.handlers;

import java.util.function.Function;

public interface AdminFunctionHandler<T,R> extends Function<T,R>, Allowable {
}
