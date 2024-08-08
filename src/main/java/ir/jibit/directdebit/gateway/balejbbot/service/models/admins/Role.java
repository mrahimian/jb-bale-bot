package ir.jibit.directdebit.gateway.balejbbot.service.models.admins;

import lombok.Getter;

import java.util.List;

import static ir.jibit.directdebit.gateway.balejbbot.service.models.admins.Permission.*;

@Getter
public enum Role {
    MANAGER(List.of(
            FETCH_STUDENTS_LIST,
            UPDATE_STUDENTS_SCORE,
            UPDATE_GIFTS_TIME,
            INSERT_STUDENTS,
            INSERT_ADMINS,
            INSERT_AWARDS,
            FETCH_MY_STUDENTS_LIST
    )),
    TEACHER(List.of(
            FETCH_STUDENTS_LIST,
            UPDATE_STUDENTS_SCORE,
            FETCH_MY_STUDENTS_LIST
    )),
    MODERATOR(List.of(FETCH_STUDENTS_LIST,
            UPDATE_STUDENTS_SCORE)),
    PRODUCER(List.of(FETCH_STUDENTS_LIST,
            UPDATE_STUDENTS_SCORE,
            UPDATE_GIFTS_TIME,
            INSERT_AWARDS)),
    SUPPLIER(List.of(FETCH_STUDENTS_LIST)),
    CONTACT_PERSON(List.of(FETCH_STUDENTS_LIST,
            INSERT_STUDENTS,
            INSERT_ADMINS));


    private final List<Permission> permissions;

    Role(List<Permission> permissions) {
        this.permissions = permissions;
    }


}
