package swiggy.interview.parking.commands;

import swiggy.interview.parking.Messages;

public enum CommandConstants implements Messages {

    CREATE("Created a parking lot with %s slots"),
    LEAVE("Slot number %s is free"),
    PARK("Allocated slot number: %s"),
    STATUS_HEADER(String.format("%-10s%-40s%-10s", "No", "Registration Slot No.", "Colour")),
    STATUS_BODY_FORMAT("%-10s%-40s%-10s");

    private String message;

    CommandConstants(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
