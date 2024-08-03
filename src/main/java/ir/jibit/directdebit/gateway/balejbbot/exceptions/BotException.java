package ir.jibit.directdebit.gateway.balejbbot.exceptions;

public class BotException extends RuntimeException{
    Error error;

    public BotException(Error error) {
        this.error = error;
    }
}
