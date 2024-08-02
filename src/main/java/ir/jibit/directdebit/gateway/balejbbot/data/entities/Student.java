package ir.jibit.directdebit.gateway.balejbbot.data.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "student")
public class Student {
    @Id
    private String id;
    private String username;
    private String password;
    private String chatId;
    private String firstName;
    private String lastName;
    private String nationalCode;
    private Instant birthDate;
    private String phoneNumber;
    private String fathersPhoneNumber;
    private String mothersPhoneNumber;
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Admin teacher;
    private int score;
    @CreationTimestamp
    private Instant createdAt;
    @UpdateTimestamp
    private Instant updatedAt;
}
