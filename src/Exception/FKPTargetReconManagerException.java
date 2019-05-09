package Exception;

public class FKPTargetReconManagerException extends Exception {
    public FKPTargetReconManagerException(Throwable throwable) {
        super(throwable);
    }

    public FKPTargetReconManagerException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public FKPTargetReconManagerException(String string) {
        super(string);
    }

    public FKPTargetReconManagerException() {
        super();
    }
}
