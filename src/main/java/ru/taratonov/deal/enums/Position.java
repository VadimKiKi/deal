package ru.taratonov.deal.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import ru.taratonov.deal.exception.IllegalArgumentOfEnumException;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
public enum Position {
    WORKER("worker"),
    MANAGER("manager"),
    TOP_MANAGER("top-manager"),
    MID_MANAGER("mid-manager"),
    OWNER("owner");

    @JsonCreator
    static Position findValue(String findValue) {
        return Arrays.stream(Position.values())
                .filter(value -> value.name().equalsIgnoreCase(findValue))
                .findFirst()
                .orElseThrow(() -> IllegalArgumentOfEnumException.createWith(
                        Arrays.stream(Position.values())
                                .map(Position::getTitle)
                                .collect(Collectors.toList())));

    }

    private final String title;

    Position(String title) {
        this.title = title;
    }

}
