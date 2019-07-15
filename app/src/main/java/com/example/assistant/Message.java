package com.example.assistant;

import java.util.Date;

public class Message {
    String text;
    Date date;
    Boolean is_sent;

    public Message(String text, Boolean is_sent) {
        this.text = text;
        this.date = new Date();
        this.is_sent = is_sent;
    }
}
