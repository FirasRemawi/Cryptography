package Project3;

public class AESException extends Exception {

    private static final long serialVersionUID = 1L;
    private String msg = "Exception occurred while processing AES operations";

    public AESException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public AESException() {
        super();
    }

    public AESException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public AESException(String message, Throwable cause) {
        super(message, cause);
    }

    public AESException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
