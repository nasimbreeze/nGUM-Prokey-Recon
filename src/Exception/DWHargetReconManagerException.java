package Exception;

public class DWHargetReconManagerException extends Exception {
    public DWHargetReconManagerException(Throwable throwable) {
        super(throwable);
    }

    public DWHargetReconManagerException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public DWHargetReconManagerException(String string) {
        super(string);
    }

    public DWHargetReconManagerException() {
        super();
    }
}
