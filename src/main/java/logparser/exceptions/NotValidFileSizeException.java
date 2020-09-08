package logparser.exceptions;

public class NotValidFileSizeException extends RuntimeException {
    private Integer maxFileSizeMb;

    public NotValidFileSizeException(Integer maxFileSizeMb) {
        this.maxFileSizeMb = maxFileSizeMb;
    }

    public String getMessage() {
        return String.format("Maximum file size is %d Mb", maxFileSizeMb);
    }
}
