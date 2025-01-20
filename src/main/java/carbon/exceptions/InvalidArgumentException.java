package carbon.exceptions;

/**
 * An InvalidArgumentException is thrown if the arguments provided for a command are invalid.
 */
public class InvalidArgumentException extends RuntimeException {
    /**
     * Constructs an InvalidArgumentException with the specified message.
     *
     * @param message The message, which may be retrieved with <code>.getMessage()</code>.
     */
    public InvalidArgumentException(String message) {
        super(message);
    }
}
