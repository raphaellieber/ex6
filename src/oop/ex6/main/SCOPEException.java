package oop.ex6.main;

/**
 * An exception that will use for exceeding the allowed depth
 */
public class SCOPEException extends Exception{

    /**
     * Constructs a new SCOPEException with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public SCOPEException() {
        super();
    }

    /**
     * Constructs a new SCOPEException with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param   message   the detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
     */
    public SCOPEException(String message) {
        super(message);
    }

    /**
     * Constructs a new SCOPEException with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this exception's detail message.
     *
     * @param  message the detail message (which is saved for later retrieval
     *         by the {@link #getMessage()} method).
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A {@code null} value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     */
    public SCOPEException(String message, Throwable cause) {
        super(message, cause);
    }
}
