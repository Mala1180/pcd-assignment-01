package app.utils;

public enum Commands {

    START("START"),
    STOP("STOP"),
    RESET("RESET"),
    OPEN_FILE_DIALOG("OPEN_FILE_DIALOG");


    private final String command;

    Commands(String command) {
        this.command = command;
    }
    @Override
    public String toString() {
        return this.command;
    }
}
