package ir.jibit.directdebit.gateway.balejbbot.service.models.students;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.Instant;

@Builder
@AllArgsConstructor
public class Student {
    private String id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String nationalCode;
    private Instant birthDate;
    private String phoneNumber;
    private String fathersPhoneNumber;
    private String mothersPhoneNumber;
    private String teacher;
    private int score;

    @Override
    public String toString() {
        return "شناسه ='" + id + '\'' +
                ", نام ='" + firstName + '\'' +
                ", نام خانوادگی ='" + lastName + '\'' +
                ", کدملی ='" + nationalCode + '\'' +
                ", تاریخ تولد =" + birthDate +
                ", شماره تلفن ='" + phoneNumber + '\'' +
                ", شماره تلفن پدر ='" + fathersPhoneNumber + '\'' +
                ", شماره تلفن مادر ='" + mothersPhoneNumber + '\'' +
                ", نام مربی ='" + teacher + '\'' +
                ", امتیاز =" + score;
    }
}
