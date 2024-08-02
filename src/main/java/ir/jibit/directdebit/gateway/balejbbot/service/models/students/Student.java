package ir.jibit.directdebit.gateway.balejbbot.service.models.students;

import ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Admin;
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
    private Admin teacher;
    private int score;

}
