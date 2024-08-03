package ir.jibit.directdebit.gateway.balejbbot.data.entities;

import ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Setter
@Getter
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
    private Instant birthDate;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    private int groupNumber;
    @CreationTimestamp
    private Instant createdAt;
    @UpdateTimestamp
    private Instant updatedAt;
}
