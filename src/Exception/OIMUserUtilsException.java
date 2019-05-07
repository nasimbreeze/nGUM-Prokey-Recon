package Exception;

public class OIMUserUtilsException extends Exception {
    public OIMUserUtilsException(Throwable throwable) {
        super(throwable);
    }

    public OIMUserUtilsException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public OIMUserUtilsException(String string) {
        super(string);
    }

    public OIMUserUtilsException() {
        super();
    }
}