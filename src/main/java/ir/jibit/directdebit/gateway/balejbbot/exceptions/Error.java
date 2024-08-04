package ir.jibit.directdebit.gateway.balejbbot.exceptions;

public enum Error {
    PERMISSION_DENIED("permission.denied","متاسفانه شما دسترسی برای انجام این کار را ندارید"),
    INSUFFICIENT_SCORE("insufficient.score","متاسفانه شما امتیاز لازم برای تهیه این جایزه را ندارید"),
    INVALID_CREDENTIALS("invalid.credentials","نام کاربری یا رمزعبور اشتباه است"),
    UNRECOGNIZED_USER("unrecognized.user","کاربر ناشناخته است")
    ;


    String errorCode;
    String message;

    Error(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
