package ir.jibit.directdebit.gateway.balejbbot.service.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Award {
    private String name;
    private int code;
    private String description;
    private int requiredScore;

    @Override
    public String toString() {
        return "عنوان = '" + name + '\'' +
                ", کد جایزه = " + code +
                getDescString() +
                ", امتیاز مورد نیاز = " + requiredScore;

    }

    private String getDescString() {
        return getDescription() == null || getDescription().isBlank() ? "" :
                ", توضیحات = '" + getDescription() + '\'';
    }
}
