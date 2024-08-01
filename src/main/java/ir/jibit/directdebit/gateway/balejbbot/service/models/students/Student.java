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
    private String chatId;
    private String firstName;
    private String lastName;
    private String nationalCode;
    private Instant birthDate;
    private String phoneNumber;
    private String FathersPhoneNumber;
    private String mothersPhoneNumber;
    private String teachesId;
    private int score;

}
