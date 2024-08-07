package ir.jibit.directdebit.gateway.balejbbot.exceptions;

import lombok.Getter;

@Getter
public class BotException extends RuntimeException {
    Error error;

    public BotException(Error error) {
        this.error = error;
    }

    public BotException(String message) {
        super(message);
    }
}
