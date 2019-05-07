package Exception;

public class InterfaceManagerException extends Exception {
    public InterfaceManagerException(Throwable throwable) {
        super(throwable);
    }

    public InterfaceManagerException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public InterfaceManagerException(String string) {
        super(string);
    }

    public InterfaceManagerException() {
        super();
    }
}
