package ir.jibit.directdebit.gateway.balejbbot.data.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "student")
public class Student {
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
    private String fathersPhoneNumber;
    private String mothersPhoneNumber;
    private String teacherId;

    @Column(nullable = false)
    private int score;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
}
