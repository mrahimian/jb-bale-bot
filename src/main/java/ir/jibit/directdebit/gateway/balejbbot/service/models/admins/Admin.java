package ir.jibit.directdebit.gateway.balejbbot.service.models.admins;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
public class Admin {
    private String id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String nationalCode;
    private String birthDate;
    private String phoneNumber;
    private Role role;
    private int groupNumber;
}
