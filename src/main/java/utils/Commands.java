package utils;

public enum Commands {

    START("START"),
    STOP("STOP"),
    RESET("RESET");


    private final String command;

    Commands(String command) {
        this.command = command;
    }
    @Override
    public String toString() {
        return this.command;
    }
}
