package ir.jibit.directdebit.gateway.balejbbot.data.entities;

import ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "admin")
public class Admin {
    @Id
    private String id;
    private String username;
    private String password;
    @Column(unique = true)
    private String chatId;
    private String firstName;
    private String lastName;
    private String nationalCode;
    private String birthDate;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private int groupNumber;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

}
