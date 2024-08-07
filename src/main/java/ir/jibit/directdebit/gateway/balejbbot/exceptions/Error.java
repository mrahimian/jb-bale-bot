package ir.jibit.directdebit.gateway.balejbbot.exceptions;

import lombok.Getter;

@Getter
public enum Error {
    PERMISSION_DENIED("permission.denied","متاسفانه شما دسترسی برای انجام این کار را ندارید"),
    INSUFFICIENT_SCORE("insufficient.score","متاسفانه شما امتیاز لازم برای تهیه این جایزه را ندارید"),
    INVALID_CREDENTIALS("invalid.credentials","نام کاربری یا رمزعبور اشتباه است"),
    UNRECOGNIZED_USER("unrecognized.user","کاربر ناشناخته است"),
    GIFTS_TIME_NOT_ACTIVE("gifts.time.not_active","کمد جوایز فعال نمی‌باشد"),
    SERVER_ERROR("server.error", "متاسفانه اشکالی در سیستم رخ داده است"),
    INVALID_INPUT("invalid.input","ورودی نامعتبر است"),
    AWARD_NOT_EXISTS("award.not_exists","کد جایزه اشتباهه \uD83D\uDE10"),
    ;


    String errorCode;
    String message;

    Error(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
