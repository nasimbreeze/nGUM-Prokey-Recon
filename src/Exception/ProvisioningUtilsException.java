package Exception;

public class ProvisioningUtilsException extends Exception {
    public ProvisioningUtilsException(Throwable throwable) {
        super(throwable);
    }

    public ProvisioningUtilsException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public ProvisioningUtilsException(String string) {
        super(string);
    }

    public ProvisioningUtilsException() {
        super();
    }
}
