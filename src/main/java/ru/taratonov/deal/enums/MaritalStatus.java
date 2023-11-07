package ru.taratonov.deal.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import ru.taratonov.deal.exception.IllegalArgumentOfEnumException;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
public enum MaritalStatus {
    DIVORCED("divorced"),
    MARRIED("married"),
    SINGLE("single"),
    WIDOW_WIDOWER("widow-widower");

    @JsonCreator
    static MaritalStatus findValue(String findValue) {
        return Arrays.stream(MaritalStatus.values())
                .filter(value -> value.name().equalsIgnoreCase(findValue))
                .findFirst()
                .orElseThrow(() -> IllegalArgumentOfEnumException.createWith(
                        Arrays.stream(MaritalStatus.values())
                                .map(MaritalStatus::getTitle)
                                .collect(Collectors.toList())));
    }

    private final String title;

    MaritalStatus(String title) {
        this.title = title;
    }

}
