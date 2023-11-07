package ru.taratonov.deal.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import ru.taratonov.deal.exception.IllegalArgumentOfEnumException;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
public enum Gender {
    MALE("male"),
    FEMALE("female"),
    NON_BINARY("non-binary");

    @JsonCreator
    static Gender findValue(String findValue) {
        return Arrays.stream(Gender.values())
                .filter(value -> value.name().equalsIgnoreCase(findValue))
                .findFirst()
                .orElseThrow(() -> IllegalArgumentOfEnumException.createWith(
                        Arrays.stream(Gender.values())
                                .map(Gender::getTitle)
                                .collect(Collectors.toList())));
    }

    private final String title;

    Gender(String title) {
        this.title = title;
    }

}
