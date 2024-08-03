package ir.jibit.directdebit.gateway.balejbbot.exceptions;

public enum Error {
    PERMISSION_DENIED("permission.denied","متاسفانه شما دسترسی برای انجام این کار را ندارید"),
    INSUFFICIENT_SCORE("insufficient.score","متاسفانه شما امتیاز لازم برای تهیه این جایزه را ندارید")
    ;


    String errorCode;
    String message;

    Error(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
