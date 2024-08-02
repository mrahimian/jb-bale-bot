package ir.jibit.directdebit.gateway.balejbbot.service.models.admins;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.Instant;

@Builder
@AllArgsConstructor

public class Admin {
    private String id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String nationalCode;
    private Instant birthDate;
    private String phoneNumber;
    private Role role;
    private int groupNumber;
}
