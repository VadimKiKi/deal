package ru.taratonov.deal.enums;

import lombok.Getter;

@Getter
public enum Theme {
    FINISH_REGISTRATION("finish-registration"),
    CREATE_DOCUMENTS("create-documents"),
    SEND_DOCUMENTS("send-documents"),
    SEND_SES("send-ses"),
    CREDIT_ISSUED("credit-issued"),
    APPLICATION_DENIED("application-denied");

    private final String title;
    Theme(String title) {
        this.title = title;
    }

}
