package logparser.exceptions;

public class NoSuchFileException extends RuntimeException {
    public String getMessage() {
        return "No such file. Check the name of your file";
    }
}