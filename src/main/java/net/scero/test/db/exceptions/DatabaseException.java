package net.scero.test.db.exceptions;

public class DatabaseException extends Exception {
    private static final long serialVersionUID = 797072712139295692L;

    /**
     * Constructor.
     */
    public DatabaseException() {
        // Nothing to do
    }

    /**
     * Constructor.
     * 
     * @param message
     */
    public DatabaseException(String message) {
        super(message);
    }

    /**
     * Constructor.
     * 
     * @param cause
     */
    public DatabaseException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor.
     * 
     * @param message
     * @param cause
     */
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor.
     * 
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public DatabaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
