package ir.jibit.directdebit.gateway.balejbbot.service.models.admins;

import lombok.Getter;

import java.util.List;

import static ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Permission.*;

@Getter
public enum Role {
    MANAGER(List.of(
            FETCH_STUDENTS_LIST,
            UPDATE_STUDENTS_SCORE,
            UPDATE_GIFTS_TIME
    )),
    TEACHER(List.of(
            FETCH_STUDENTS_LIST,
            UPDATE_STUDENTS_SCORE
    )),
    MODERATOR(List.of(FETCH_STUDENTS_LIST,
            UPDATE_STUDENTS_SCORE)),
    PRODUCER(List.of(FETCH_STUDENTS_LIST,
            UPDATE_STUDENTS_SCORE,
            UPDATE_GIFTS_TIME)),
    SUPPLIER(List.of(FETCH_STUDENTS_LIST)),
    CONTACT_PERSON(List.of(FETCH_STUDENTS_LIST));


    private final List<Permission> permissions;

    Role(List<Permission> permissions) {
        this.permissions = permissions;
    }


    }
