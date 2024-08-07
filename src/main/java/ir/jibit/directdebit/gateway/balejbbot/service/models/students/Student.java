package ir.jibit.directdebit.gateway.balejbbot.service.models.students;

import com.github.mfathi91.time.PersianDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Student {
    private String id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String nationalCode;
    private String birthDate;
    private String phoneNumber;
    private String fathersPhoneNumber;
    private String mothersPhoneNumber;
    private String teacher;
    private int score;

    public Student(String id, String username, String password, String firstName, String lastName, String nationalCode,
                   String birthDate, String phoneNumber, String fathersPhoneNumber, String mothersPhoneNumber, String teacher) {

        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationalCode = nationalCode;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.fathersPhoneNumber = fathersPhoneNumber;
        this.mothersPhoneNumber = mothersPhoneNumber;
        this.teacher = teacher;
    }

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
