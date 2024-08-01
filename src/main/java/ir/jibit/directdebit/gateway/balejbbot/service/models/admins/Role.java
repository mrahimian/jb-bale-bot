package ir.jibit.directdebit.gateway.balejbbot.service.models.admins;

import java.util.List;

import static ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Permission.*;

public enum Role {
    MANAGER(List.of(
            FETCH_STUDENTS_LIST,
            INCREASE_STUDENTS_SCORE,
            DECREASE_STUDENTS_SCORE,
            ENABLE_GIFTS_TIME,
            DISABLE_GIFTS_TIME
    )),
    TEACHER(List.of(
            FETCH_STUDENTS_LIST,
            INCREASE_STUDENTS_SCORE,
            DECREASE_STUDENTS_SCORE
    )),
    MODERATOR(List.of(FETCH_STUDENTS_LIST,
            INCREASE_STUDENTS_SCORE,
            DECREASE_STUDENTS_SCORE)),
    PRODUCER(List.of(FETCH_STUDENTS_LIST,
            INCREASE_STUDENTS_SCORE,
            DECREASE_STUDENTS_SCORE,
            ENABLE_GIFTS_TIME,
            DISABLE_GIFTS_TIME)),
    SUPPLIER(List.of(FETCH_STUDENTS_LIST)),
    CONTACT_PERSON(List.of(FETCH_STUDENTS_LIST));


    private final List<Permission> permissions;

    Role(List<Permission> permissions) {
        this.permissions = permissions;
    }


    }
