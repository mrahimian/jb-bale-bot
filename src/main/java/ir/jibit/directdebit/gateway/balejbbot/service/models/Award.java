package ir.jibit.directdebit.gateway.balejbbot.service.models;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Award {
    private String id;
    private String name;
    private int code;
    private String description;
    private int requiredScore;
}
